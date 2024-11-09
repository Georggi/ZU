package com.georggi.zu;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;
import com.georggi.zu.mixins.Mixins;

import java.util.List;
import java.util.Set;

@LateMixin
public class ZULateMixins implements ILateMixinLoader {

    @Override
    public String getMixinConfig() {
        return "mixins.zu.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        return Mixins.getLateMixins(loadedMods);
    }
}
