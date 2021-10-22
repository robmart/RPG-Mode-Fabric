package robmart.mod.rpgmodecreatures.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;

import java.util.ArrayList;
import java.util.List;

public class MetaEntityGroup extends EntityGroup {
    public final List<EntityGroup> subGroups = new ArrayList<>();
}
