package robmart.mod.rpgmodecreatures.common.entity;

import net.minecraft.entity.EntityGroup;

public class RPGEntityGroup { //TODO: Change group of vanilla stuff
    public static final MetaEntityGroup ABERRATION = new MetaEntityGroup();
    public static final EntityGroup FEY = new EntityGroup();
    public static final MetaEntityGroup ELEMENTAL = new MetaEntityGroup();
    public static final EntityGroup FIRE = new EntityGroup();
    public static final EntityGroup WATER = new EntityGroup();
    public static final EntityGroup EARTH = new EntityGroup();
    public static final EntityGroup AIR = new EntityGroup();
    public static final MetaEntityGroup FIEND = new MetaEntityGroup();
    public static final EntityGroup DEMON = new EntityGroup();
    public static final EntityGroup DEVIL = new EntityGroup();
    public static final MetaEntityGroup BEAST = new MetaEntityGroup();
    public static final EntityGroup CELESTIAL = new EntityGroup();
    public static final EntityGroup CONSTRUCT = new EntityGroup();
    public static final EntityGroup DRAGON = new EntityGroup();
    public static final EntityGroup GIANT = new EntityGroup();
    public static final EntityGroup HUMANOID = new EntityGroup();
    public static final EntityGroup MONSTROSITY = new EntityGroup();
    public static final EntityGroup OOZE = new EntityGroup();
    public static final EntityGroup PLANT = new EntityGroup();

    static {
        ABERRATION.subGroups.add(EntityGroup.ILLAGER);

        ELEMENTAL.subGroups.add(FIRE);
        ELEMENTAL.subGroups.add(WATER);
        ELEMENTAL.subGroups.add(EARTH);
        ELEMENTAL.subGroups.add(AIR);

        FIEND.subGroups.add(DEMON);
        FIEND.subGroups.add(DEVIL);

        BEAST.subGroups.add(EntityGroup.AQUATIC);
    }
}
