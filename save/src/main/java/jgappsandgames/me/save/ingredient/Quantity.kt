package jgappsandgames.me.save.ingredient

// JSON
import org.json.JSONObject

/**
 * QuantityClass
 * Created by Joshua Garner on 3/1/2018.
 */
class Quantity() {
    // Enums ---------------------------------------------------------------------------------------
    enum class Unit {
        NONE,
        TEASPOON, TABLESPOON, CUP, FLUID_OUNCE, PINT, QUART, GALLON, OUNCE, POUND,
        MILLILITER, LITER, GRAM, KILOGRAM;

        companion object {
            fun toInt(unit: Unit?): Int {
                if (unit == null) return 0

                return when (unit) {
                    Unit.NONE  -> 0
                    Unit.TEASPOON  -> 1
                    Unit.TABLESPOON  -> 2
                    Unit.CUP  -> 3
                    Unit.FLUID_OUNCE  -> 4
                    Unit.PINT  -> 5
                    Unit.QUART  -> 6
                    Unit.GALLON  -> 7
                    Unit.OUNCE  -> 8
                    Unit.POUND  -> 9
                    Unit.MILLILITER  -> 10
                    Unit.LITER  -> 11
                    Unit.GRAM  -> 12
                    Unit.KILOGRAM  -> 13
                }
            }

            fun fromInt(int: Int): Unit {
                return when (int) {
                    0 -> Unit.NONE
                    1 -> Unit.TEASPOON
                    2 -> Unit.TABLESPOON
                    3 -> Unit.CUP
                    4 -> Unit.FLUID_OUNCE
                    5 -> Unit.PINT
                    6 -> Unit.QUART
                    7 -> Unit.GALLON
                    8 -> Unit.OUNCE
                    9 -> Unit.POUND
                    10 -> Unit.MILLILITER
                    11 -> Unit.LITER
                    12 -> Unit.GRAM
                    13 -> Unit.KILOGRAM
                    else -> Unit.NONE
                }
            }

            fun unitToString(unit: Unit?): String {
                if (unit == null) return ""
                return when (unit) {
                    Unit.NONE  -> ""
                    Unit.TEASPOON  -> "Teaspoon"
                    Unit.TABLESPOON  -> "Tablespoon"
                    Unit.CUP  -> "Cup"
                    Unit.FLUID_OUNCE  -> "Fluid Ounce"
                    Unit.PINT  -> "Pint"
                    Unit.QUART  -> "Quart"
                    Unit.GALLON  -> "Gallon"
                    Unit.OUNCE  -> "Ounce"
                    Unit.POUND  -> "Pound"
                    Unit.MILLILITER  -> "Milliliter"
                    Unit.LITER  -> "Liter"
                    Unit.GRAM  -> "Gram"
                    Unit.KILOGRAM  -> "Kilogram"
                }
            }

            fun unitToList(): List<String> {
                return listOf("None",
                        "Teaspoon", "Tablespoon", "Cup", "Fluid Ounce", "Pint", "Quart", "Gallon", "Ounce", "Pound",
                        "Milliliter", "Liter", "Gram", "Kilogram")
            }
        }
    }

    // Constants -----------------------------------------------------------------------------------
    companion object {
        private const val AMOUNT = "amount"
        private const val UNIT = "unit"
    }

    // Data ----------------------------------------------------------------------------------------
    private var amount: Double = 0.0
    private var unit: Unit = Unit.NONE

    // Constructors --------------------------------------------------------------------------------
    constructor(amount: Double, unit: Unit): this() {
        this.amount = amount
        this.unit = unit
    }

    constructor(data: JSONObject): this() {
        this.amount = data.optDouble(AMOUNT, 0.0)
        this.unit = Unit.fromInt(data.optInt(UNIT, 0))
    }

    // Getters -------------------------------------------------------------------------------------
    fun getAmount(): Double {
        return amount
    }

    fun getUnit(): Unit {
        return unit
    }

    // Setters -------------------------------------------------------------------------------------
    fun setAmount(_amount: Double): Quantity {
        amount = _amount
        return this
    }

    fun setUnit(_unit: Unit): Quantity {
        unit = _unit
        return this
    }

    // To Methods ----------------------------------------------------------------------------------
    override fun toString(): String {
        return toJSON().toString()
    }

    fun toJSON(): JSONObject {
        val data = JSONObject()
        data.put(AMOUNT, amount)
        data.put(UNIT, Unit.toInt(unit))
        return data
    }

    fun toTeaspoon(): Quantity {
        return when (unit) {
            Unit.TEASPOON -> this
            Unit.TABLESPOON -> Quantity(amount * 3, Unit.TEASPOON)
            Unit.CUP -> Quantity(amount * 48, Unit.TEASPOON)
            Unit.FLUID_OUNCE -> Quantity(amount * 6, Unit.TEASPOON)
            Unit.PINT -> Quantity(amount * 96, Unit.TEASPOON)
            Unit.QUART -> Quantity(amount * 192, Unit.TEASPOON)
            Unit.GALLON -> Quantity(amount * 768, Unit.TEASPOON)
            Unit.MILLILITER -> Quantity(amount * 0.2, Unit.TEASPOON)
            Unit.LITER -> Quantity(amount * 200, Unit.TEASPOON)
            else -> this
        }
    }

    fun toTablespoon(): Quantity {
        return when (unit) {
            Unit.TEASPOON -> Quantity(amount * 0.33333, Unit.TABLESPOON)
            Unit.TABLESPOON -> this
            Unit.CUP -> Quantity(amount * 16, Unit.TABLESPOON)
            Unit.FLUID_OUNCE -> Quantity(amount * 2, Unit.TABLESPOON)
            Unit.PINT -> Quantity(amount * 32, Unit.TABLESPOON)
            Unit.QUART -> Quantity(amount * 64, Unit.TABLESPOON)
            Unit.GALLON -> Quantity(amount * 256, Unit.TABLESPOON)
            Unit.MILLILITER -> Quantity(amount * 0.06666667, Unit.TABLESPOON)
            Unit.LITER -> Quantity(amount * 66.6666666667, Unit.TABLESPOON)
            else -> this
        }
    }

    fun toFluidOunce(): Quantity {
        return when (unit) {
            Unit.TEASPOON -> Quantity(amount * 0.166666666667, Unit.FLUID_OUNCE)
            Unit.TABLESPOON -> Quantity(amount * 0.5, Unit.FLUID_OUNCE)
            Unit.CUP -> Quantity(amount * 8, Unit.FLUID_OUNCE)
            Unit.FLUID_OUNCE -> this
            Unit.PINT -> Quantity(amount * 16, Unit.FLUID_OUNCE)
            Unit.QUART -> Quantity(amount * 32, Unit.FLUID_OUNCE)
            Unit.GALLON -> Quantity(amount * 128, Unit.FLUID_OUNCE)
            Unit.MILLILITER -> Quantity(amount * 0.0333333333, Unit.FLUID_OUNCE)
            Unit.LITER -> Quantity(amount * 33.33333333, Unit.FLUID_OUNCE)
            else -> this
        }
    }

    fun toCup(): Quantity {
        return when (unit) {
            Unit.TEASPOON -> Quantity(amount * 0.0208333, Unit.CUP)
            Unit.TABLESPOON -> Quantity(amount * 0.0625, Unit.CUP)
            Unit.CUP -> this
            Unit.FLUID_OUNCE -> Quantity(amount * 0.125, Unit.CUP)
            Unit.PINT -> Quantity(amount * 2, Unit.CUP)
            Unit.QUART -> Quantity(amount * 4, Unit.CUP)
            Unit.GALLON -> Quantity(amount * 16, Unit.CUP)
            Unit.MILLILITER -> Quantity(amount * 0.0041666667, Unit.CUP)
            Unit.LITER -> Quantity(amount * 4.166667, Unit.CUP)
            else -> this
        }
    }

    fun toPint(): Quantity {
        return when (unit) {
            Unit.TEASPOON -> Quantity(amount * 0.10416666666667, Unit.PINT)
            Unit.TABLESPOON -> Quantity(amount * 0.03125, Unit.PINT)
            Unit.CUP -> Quantity(amount * 0.5, Unit.PINT)
            Unit.FLUID_OUNCE -> Quantity(amount * 0.0625, Unit.PINT)
            Unit.PINT -> this
            Unit.QUART -> Quantity(amount * 2, Unit.PINT)
            Unit.GALLON -> Quantity(amount * 8, Unit.PINT)
            Unit.MILLILITER -> Quantity(amount * 0.002088888888888888889, Unit.PINT)
            Unit.LITER -> Quantity(amount * 2.08888888889, Unit.PINT)
            else -> this
        }
    }

    fun toQuart(): Quantity {
        return when (unit) {
            Unit.TEASPOON -> Quantity(amount * 0.052088888888889, Unit.QUART)
            Unit.TABLESPOON -> Quantity(amount * 0.015625, Unit.QUART)
            Unit.CUP -> Quantity(amount * 0.25, Unit.QUART)
            Unit.FLUID_OUNCE -> Quantity(amount * 0.0032825, Unit.QUART)
            Unit.PINT -> Quantity(amount * 0.5, Unit.QUART)
            Unit.QUART -> this
            Unit.GALLON -> Quantity(amount * 4, Unit.QUART)
            Unit.MILLILITER -> Quantity(amount * 0.0010444444444444444444444, Unit.QUART)
            Unit.LITER -> Quantity(amount * 1.0444444444444, Unit.QUART)
            else -> this
        }
    }

    fun toGallon(): Quantity {
        return when (unit) {
            Unit.TEASPOON -> Quantity(amount * 0.001302, Unit.GALLON)
            Unit.TABLESPOON -> Quantity(amount * 0.00390625, Unit.GALLON)
            Unit.CUP -> Quantity(amount * 0.0625, Unit.GALLON)
            Unit.FLUID_OUNCE -> Quantity(amount * 0.0078125, Unit.GALLON)
            Unit.PINT -> Quantity(amount * 0.125, Unit.GALLON)
            Unit.QUART -> Quantity(amount * 0.25, Unit.GALLON)
            Unit.GALLON -> this
            Unit.MILLILITER -> toQuart().toMilliliter()
            Unit.LITER -> Quantity(amount * 0.26, Unit.GALLON)
            else -> this
        }
    }

    fun toMilliliter(): Quantity {
        return when (unit) {
            Unit.TEASPOON -> Quantity(amount * 5, Unit.MILLILITER)
            Unit.TABLESPOON -> Quantity(amount * 15, Unit.MILLILITER)
            Unit.CUP -> Quantity(amount * 240, Unit.MILLILITER)
            Unit.FLUID_OUNCE -> Quantity(amount * 30, Unit.MILLILITER)
            Unit.PINT -> Quantity(amount * 470, Unit.MILLILITER)
            Unit.QUART -> Quantity(amount * 950, Unit.MILLILITER)
            Unit.GALLON -> Quantity(amount * 3800, Unit.MILLILITER)
            Unit.MILLILITER -> this
            Unit.LITER -> Quantity(amount * 1000, Unit.MILLILITER)
            else -> this
        }
    }

    fun toLiter(): Quantity {
        return when (unit) {
            Unit.TEASPOON -> Quantity(amount * 0.005, Unit.LITER)
            Unit.TABLESPOON -> Quantity(amount * 0.015, Unit.LITER)
            Unit.CUP -> Quantity(amount * 0.24, Unit.LITER)
            Unit.FLUID_OUNCE -> Quantity(amount * 0.03, Unit.LITER)
            Unit.PINT -> Quantity(amount * 0.47, Unit.LITER)
            Unit.QUART -> Quantity(amount * 0.95, Unit.LITER)
            Unit.GALLON -> Quantity(amount * 3.8, Unit.LITER)
            Unit.MILLILITER -> Quantity(amount / 1000, Unit.LITER)
            else -> this
        }
    }

    fun toOunce(): Quantity {
        return when (unit) {
            Unit.OUNCE -> this
            Unit.POUND -> Quantity(amount * 16, Unit.OUNCE)
            Unit.GRAM -> Quantity(amount * 0.035, Unit.OUNCE)
            Unit.KILOGRAM -> Quantity(amount * 35, Unit.OUNCE)
            else -> this
        }
    }

    fun toPound(): Quantity {
        return when (unit) {
            Unit.OUNCE -> Quantity(amount / 16, Unit.POUND)
            Unit.POUND -> this
            Unit.GRAM -> Quantity(amount / 454, Unit.POUND)
            Unit.KILOGRAM -> Quantity(amount * 2.205, Unit.POUND)
            else -> this
        }
    }

    fun toGram(): Quantity {
        return when (unit) {
            Unit.OUNCE -> Quantity(amount * 28, Unit.GRAM)
            Unit.POUND -> Quantity(amount * 454, Unit.GRAM)
            Unit.GRAM -> this
            Unit.KILOGRAM -> Quantity(amount * 1000, Unit.GRAM)
            else -> this
        }
    }

    fun toKilogram(): Quantity {
        return when (unit) {
            Unit.OUNCE -> Quantity(amount / 35, Unit.KILOGRAM)
            Unit.POUND -> Quantity(amount / 2.205, Unit.KILOGRAM)
            Unit.GRAM -> Quantity(amount * 1000, Unit.KILOGRAM)
            else -> this
        }
    }
}