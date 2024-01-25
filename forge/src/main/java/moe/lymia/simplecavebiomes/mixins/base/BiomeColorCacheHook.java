package moe.lymia.simplecavebiomes.mixins.base;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import moe.lymia.simplecavebiomes.world.BiomeColorCacheClasses.CubicColors;
import moe.lymia.simplecavebiomes.world.BiomeColorCacheClasses.CubicLast;
import net.minecraft.client.world.BiomeColorCache;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import org.spongepowered.asm.mixin.*;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.IntSupplier;

import static moe.lymia.simplecavebiomes.world.BiomeColorCacheClasses.MAX_ENTRY_SIZE;

/**
 * Replaces the implementation of BiomeColorCache with one derived from MC 1.20.4
 * <p>
 * Everything here is an overwrite because, uh... This amounts to a class replacement.
 */
@Mixin(BiomeColorCache.class)
public class BiomeColorCacheHook {
    @Unique
    private final ThreadLocal<CubicLast> last = ThreadLocal.withInitial(CubicLast::new);

    @Unique
    private final Long2ObjectLinkedOpenHashMap<CubicColors> colors =
            new Long2ObjectLinkedOpenHashMap<>(MAX_ENTRY_SIZE, 0.25F);

    @Shadow
    @Final
    private ReentrantReadWriteLock lock;

    @Overwrite
    public int getBiomeColor(BlockPos pos, IntSupplier colorFactory) {
        int chunkX = ChunkSectionPos.getSectionCoord(pos.getX());
        int chunkZ = ChunkSectionPos.getSectionCoord(pos.getZ());
        CubicLast last = this.last.get();
        if (last.x != chunkX || last.z != chunkZ || last.colors == null || last.colors.needsCacheRefresh()) {
            last.x = chunkX;
            last.z = chunkZ;
            last.colors = this.scb$getColorArray(chunkX, chunkZ);
        }

        int[] colorCache = last.colors.get(pos.getY());
        int xOff = pos.getX() & 15;
        int yOff = pos.getZ() & 15;
        int idx = yOff << 4 | xOff;
        int cachedColor = colorCache[idx];
        if (cachedColor != -1) {
            return cachedColor;
        } else {
            int newColor = colorFactory.getAsInt();
            colorCache[idx] = newColor;
            return newColor;
        }
    }

    @Overwrite
    public void reset(int chunkX, int chunkZ) {
        try {
            this.lock.writeLock().lock();
            for (int k = -1; k <= 1; ++k) {
                for (int l = -1; l <= 1; ++l) {
                    long m = ChunkPos.toLong(chunkX + k, chunkZ + l);
                    CubicColors oldColors = this.colors.remove(m);
                    if (oldColors != null) oldColors.setNeedsCacheRefresh();
                }
            }
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Overwrite
    public void reset() {
        try {
            this.lock.writeLock().lock();
            this.colors.values().forEach(CubicColors::setNeedsCacheRefresh);
            this.colors.clear();
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Unique
    private CubicColors scb$getColorArray(int chunkX, int chunkZ) {
        long encodedChunkPos = ChunkPos.toLong(chunkX, chunkZ);
        this.lock.readLock().lock();

        try {
            CubicColors colors = this.colors.get(encodedChunkPos);
            if (colors != null) {
                return colors;
            }
        } finally {
            this.lock.readLock().unlock();
        }

        this.lock.writeLock().lock();

        try {
            CubicColors colors = this.colors.get(encodedChunkPos);
            if (colors == null) {
                CubicColors newColors = new CubicColors();
                if (this.colors.size() >= MAX_ENTRY_SIZE) {
                    CubicColors oldColors = this.colors.removeFirst();
                    if (oldColors != null) {
                        oldColors.setNeedsCacheRefresh();
                    }
                }
                this.colors.put(encodedChunkPos, newColors);
                return newColors;
            }
            return colors;
        } finally {
            this.lock.writeLock().unlock();
        }
    }
}
