Interfaces:
interfaces für z. B.:
Boat sollte auch damage erhalten, wie in MC, aber Boat !extends LivingEntity
Da Boat extends VehicleEntity, kann VehicleEntity auch das Interfaces Damageable implementen ohne Vererbung
-------------
Tickable          tick()                            -> alles was geupdatet werden muss
                                                    (z. B. entities, chunks, blocks, ...)
Damageable        damage(), getHealth(), die()      -> alles was schaden nehmen kann
Rideable          addPassenger(), removePassenger() -> alles was von einem spieler oder sogar mob
                                                    gemountet werden kann
Interactable      onInteract(Player player)         -> bei rechtsclick: kisten, villager, redstone??, ...
Collidable        getAABB()                         -> für PhysicsEngine.java später, Hitbox etc
-------------

Klassen:
abstrakte Klassen für Konzepte und die spezifischen für die Implementation, sowas wie
// NICHT:
Entity e = new Entity();

// sondern
Entity e = new ZombieEntity();
-------------
Entity        (abs)          <- base Klasse          <-  implements Tickable, Collidable
LivingEntity  (abs)          <- extends Entity       <- implements Damageable
Player                       <- extends LivingEntity <- implements Interactable
Mob           (abs)          <- extends LivingEntity
PassiveMob    (abs)          <- extends Mob
HostileMob    (abs)          <- extends Mob
ItemEntity                   <- extends Entity
ProjectileEntity             <- extends Entity
VehicleEntity (abs)          <- extends Entity        <- implements Rideable, Damageable    
BoatEntity                   <- extends VehicleEntity
MinecartEntity               <- extends VehicleEntity
-------------