package moe.lymia.simplecavebiomes;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Used to mark that this mod provides cavebiomeapi compatible API.
 */
@Mod(value = CaveBiomeAPIShim.MOD_ID)
public class CaveBiomeAPIShim {
    public static final String MOD_ID = "cavebiomeapi";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
}
