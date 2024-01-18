package moe.lymia.simplecavebiomes.world;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@OnlyIn(Dist.CLIENT)
public class BiomeColorCacheClasses {
    public static final int MAX_ENTRY_SIZE = 256;

    @OnlyIn(Dist.CLIENT)
    public static class CubicColors {
        private final Int2ObjectArrayMap<int[]> colors = new Int2ObjectArrayMap<>(16);
        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        private static final int XZ_COLORS_SIZE = 16 * 16;
        private volatile boolean needsCacheRefresh;

        public int[] get(int y) {
            this.lock.readLock().lock();
            try {
                int[] data = this.colors.get(y);
                if (data != null) return data;
            } finally {
                this.lock.readLock().unlock();
            }

            this.lock.writeLock().lock();
            try {
                return this.colors.computeIfAbsent(y, yx -> createDefault());
            } finally {
                this.lock.writeLock().unlock();
            }
        }

        private static int[] createDefault() {
            int[] is = new int[XZ_COLORS_SIZE];
            Arrays.fill(is, -1);
            return is;
        }

        public boolean needsCacheRefresh() {
            return this.needsCacheRefresh;
        }

        public void setNeedsCacheRefresh() {
            this.needsCacheRefresh = true;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class CubicLast {
        public int x = Integer.MIN_VALUE;
        public int z = Integer.MIN_VALUE;
        @Nullable
        public CubicColors colors;

        public CubicLast() {
        }
    }
}
