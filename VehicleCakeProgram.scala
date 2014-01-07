// Kind of based off this: https://gist.github.com/danclien/7094990
// =====================
// service interfaces

trait EngineComponent {
    val engine: Engine
    trait Engine {
        def on: Unit
        def off: Unit
        def energy_source: String
        def topSpeed: Int
    }
}

trait ShopComponent {
    val shop: Shop
    trait Shop {
        def name: String
    }
}

// =====================
// service implementations
trait HumanPedalEngineComponentImpl extends EngineComponent {
    class HumanPedalEngine extends Engine {
        def on = println("  started pedaling...")
        def off = println(  "stopped pedaling")
        def energy_source = "legs"
        def topSpeed = 20
    }
}

trait CombustionEngineComponentImpl extends EngineComponent {
    class CombustionEngine extends Engine {
        def on = println("  combustion engine on")
        def off = println("  combustion engine off")
        def energy_source = "gasoline"
        def topSpeed = 150
    }
}

trait ElectricEngineComponentImpl extends EngineComponent {
    class ElectricEngine extends Engine {
        def on = println("  electric engine on")
        def off = println("  electric engine off")
        def energy_source = "electric battery"
        def topSpeed = 80
    }
}

/***** Dependent implementation *****/
trait VehicleComponentImpl {
    this: EngineComponent =>

    class Vehicle {
        def start = {
            println("  Attempting to start the vehicle...")
            engine.on
            println("  using " + engine.energy_source)
        }
        def milesDriven(minutes: Int) : Int = {
            engine.topSpeed * minutes / 60
        }
    }
}

trait MyShopComponentImpl extends ShopComponent {
    class MyShop(storeName: String) extends Shop {
        def name: String = {
            "Welcome to " + storeName
        }
        override def toString() : String = {
            storeName
        }
    }
}

// ======================
// get service from registry
object BikeStoreRegistry extends HumanPedalEngineComponentImpl
    with VehicleComponentImpl
    with MyShopComponentImpl
{
    val engine = new HumanPedalEngine
    val vehicle = new Vehicle

    val shop = new MyShop("Bike Store")
}

object FordDealershipRegistry extends CombustionEngineComponentImpl
    with VehicleComponentImpl
    with MyShopComponentImpl
{
    val engine = new CombustionEngine
    val vehicle = new Vehicle

    val shop = new MyShop("Ford Dealership")
}

object TeslaDealershipRegistry extends ElectricEngineComponentImpl
    with VehicleComponentImpl
    with MyShopComponentImpl
{
    val engine = new ElectricEngine
    val vehicle = new Vehicle

    val shop = new MyShop("Tesla Dealership")
}

object VehicleCakeProgram {
    def main(args: Array[String]) {

        val x = 30

        // You can replace registry objects at will.  For example I have 3 in this case
        println(FordDealershipRegistry.shop.name)
        val v1 = FordDealershipRegistry.vehicle 
        v1.start
        println("  Miles driven in %d minutes is %d".format(x, v1.milesDriven(x)))
        println()

        println(TeslaDealershipRegistry.shop.name)
        val v2 = TeslaDealershipRegistry.vehicle
        v2.start
        println("  Miles driven in %d minutes is %d".format(x, v2.milesDriven(x)))
        println()

        println(BikeStoreRegistry.shop.name)
        val v3 = BikeStoreRegistry.vehicle
        v3.start
        println("  Miles driven in %d minutes is %d".format(x, v3.milesDriven(x)))
        println()
    }
}


