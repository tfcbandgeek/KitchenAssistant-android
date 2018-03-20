package jgappsandgames.me.save.ingredient

// JSON
import org.json.JSONObject

/**
 * QuanityClass
 * Created by Joshua Garner on 3/1/2018.
 */
class Quanity() {
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
    fun setAmount(_amount: Double): Quanity {
        amount = _amount
        return this
    }

    fun setUnit(_unit: Unit): Quanity {
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

    fun toTeaspoon(): Quanity {
        return when (unit) {
            Unit.TEASPOON -> this
            Unit.TABLESPOON -> Quanity(amount * 3, Unit.TEASPOON)
            Unit.CUP -> Quanity(amount * 48, Unit.TEASPOON)
            Unit.FLUID_OUNCE -> Quanity(amount * 6, Unit.TEASPOON)
            Unit.PINT -> Quanity(amount * 96, Unit.TEASPOON)
            Unit.QUART -> Quanity(amount * 192, Unit.TEASPOON)
            Unit.GALLON -> Quanity(amount * 768, Unit.TEASPOON)
            Unit.MILLILITER -> Quanity(amount * 0.2, Unit.TEASPOON)
            Unit.LITER -> Quanity(amount * 200, Unit.TEASPOON)
            else -> this
        }
    }

    fun toTablespoon(): Quanity {
        return when (unit) {
            Unit.TEASPOON -> Quanity(amount * 0.33333, Unit.TABLESPOON)
            Unit.TABLESPOON -> this
            Unit.CUP -> Quanity(amount * 16, Unit.TABLESPOON)
            Unit.FLUID_OUNCE -> Quanity(amount * 2, Unit.TABLESPOON)
            Unit.PINT -> Quanity(amount * 32, Unit.TABLESPOON)
            Unit.QUART -> Quanity(amount * 64, Unit.TABLESPOON)
            Unit.GALLON -> Quanity(amount * 256, Unit.TABLESPOON)
            Unit.MILLILITER -> Quanity(amount * 0.06666667, Unit.TABLESPOON)
            Unit.LITER -> Quanity(amount * 66.6666666667, Unit.TABLESPOON)
            else -> this
        }
    }

    fun toFluidOunce(): Quanity {
        return when (unit) {
            Unit.TEASPOON -> Quanity(amount * 0.166666666667, Unit.FLUID_OUNCE)
            Unit.TABLESPOON -> Quanity(amount * 0.5, Unit.FLUID_OUNCE)
            Unit.CUP -> Quanity(amount * 8, Unit.FLUID_OUNCE)
            Unit.FLUID_OUNCE -> this
            Unit.PINT -> Quanity(amount * 16, Unit.FLUID_OUNCE)
            Unit.QUART -> Quanity(amount * 32, Unit.FLUID_OUNCE)
            Unit.GALLON -> Quanity(amount * 128, Unit.FLUID_OUNCE)
            Unit.MILLILITER -> Quanity(amount * 0.0333333333, Unit.FLUID_OUNCE)
            Unit.LITER -> Quanity(amount * 33.33333333, Unit.FLUID_OUNCE)
            else -> this
        }
    }

    fun toCup(): Quanity {
        return when (unit) {
            Unit.TEASPOON -> Quanity(amount * 0.0208333, Unit.CUP)
            Unit.TABLESPOON -> Quanity(amount * 0.0625, Unit.CUP)
            Unit.CUP -> this
            Unit.FLUID_OUNCE -> Quanity(amount * 0.125, Unit.CUP)
            Unit.PINT -> Quanity(amount * 2, Unit.CUP)
            Unit.QUART -> Quanity(amount * 4, Unit.CUP)
            Unit.GALLON -> Quanity(amount * 16, Unit.CUP)
            Unit.MILLILITER -> Quanity(amount * 0.0041666667, Unit.CUP)
            Unit.LITER -> Quanity(amount * 4.166667, Unit.CUP)
            else -> this
        }
    }

    fun toPint(): Quanity {
        return when (unit) {
            Unit.TEASPOON -> Quanity(amount * 0.10416666666667, Unit.PINT)
            Unit.TABLESPOON -> Quanity(amount * 0.03125, Unit.PINT)
            Unit.CUP -> Quanity(amount * 0.5, Unit.PINT)
            Unit.FLUID_OUNCE -> Quanity(amount * 0.0625, Unit.PINT)
            Unit.PINT -> this
            Unit.QUART -> Quanity(amount * 2, Unit.PINT)
            Unit.GALLON -> Quanity(amount * 8, Unit.PINT)
            Unit.MILLILITER -> Quanity(amount * 0.002088888888888888889, Unit.PINT)
            Unit.LITER -> Quanity(amount * 2.08888888889, Unit.PINT)
            else -> this
        }
    }

    fun toQuart(): Quanity {
        return when (unit) {
            Unit.TEASPOON -> Quanity(amount * 0.052088888888889, Unit.QUART)
            Unit.TABLESPOON -> Quanity(amount * 0.015625, Unit.QUART)
            Unit.CUP -> Quanity(amount * 0.25, Unit.QUART)
            Unit.FLUID_OUNCE -> Quanity(amount * 0.0032825, Unit.QUART)
            Unit.PINT -> Quanity(amount * 0.5, Unit.QUART)
            Unit.QUART -> this
            Unit.GALLON -> Quanity(amount * 4, Unit.QUART)
            Unit.MILLILITER -> Quanity(amount * 0.0010444444444444444444444, Unit.QUART)
            Unit.LITER -> Quanity(amount * 1.0444444444444, Unit.QUART)
            else -> this
        }
    }

    fun toGallon(): Quanity {
        return when (unit) {
            Unit.TEASPOON -> Quanity(amount * 0.001302, Unit.GALLON)
            Unit.TABLESPOON -> Quanity(amount * 0.00390625, Unit.GALLON)
            Unit.CUP -> Quanity(amount * 0.0625, Unit.GALLON)
            Unit.FLUID_OUNCE -> Quanity(amount * 0.0078125, Unit.GALLON)
            Unit.PINT -> Quanity(amount * 0.125, Unit.GALLON)
            Unit.QUART -> Quanity(amount * 0.25, Unit.GALLON)
            Unit.GALLON -> this
            Unit.MILLILITER -> toQuart().toMilliliter()
            Unit.LITER -> Quanity(amount * 0.26, Unit.GALLON)
            else -> this
        }
    }

    fun toMilliliter(): Quanity {
        return when (unit) {
            Unit.TEASPOON -> Quanity(amount * 5, Unit.MILLILITER)
            Unit.TABLESPOON -> Quanity(amount * 15, Unit.MILLILITER)
            Unit.CUP -> Quanity(amount * 240, Unit.MILLILITER)
            Unit.FLUID_OUNCE -> Quanity(amount * 30, Unit.MILLILITER)
            Unit.PINT -> Quanity(amount * 470, Unit.MILLILITER)
            Unit.QUART -> Quanity(amount * 950, Unit.MILLILITER)
            Unit.GALLON -> Quanity(amount * 3800, Unit.MILLILITER)
            Unit.MILLILITER -> this
            Unit.LITER -> Quanity(amount * 1000, Unit.MILLILITER)
            else -> this
        }
    }

    fun toLiter(): Quanity {
        return when (unit) {
            Unit.TEASPOON -> Quanity(amount * 0.005, Unit.LITER)
            Unit.TABLESPOON -> Quanity(amount * 0.015, Unit.LITER)
            Unit.CUP -> Quanity(amount * 0.24, Unit.LITER)
            Unit.FLUID_OUNCE -> Quanity(amount * 0.03, Unit.LITER)
            Unit.PINT -> Quanity(amount * 0.47, Unit.LITER)
            Unit.QUART -> Quanity(amount * 0.95, Unit.LITER)
            Unit.GALLON -> Quanity(amount * 3.8, Unit.LITER)
            Unit.MILLILITER -> Quanity(amount / 1000, Unit.LITER)
            else -> this
        }
    }

    fun toOunce(): Quanity {
        return when (unit) {
            Unit.OUNCE -> this
            Unit.POUND -> Quanity(amount * 16, Unit.OUNCE)
            Unit.GRAM -> Quanity(amount * 0.035, Unit.OUNCE)
            Unit.KILOGRAM -> Quanity(amount * 35, Unit.OUNCE)
            else -> this
        }
    }

    fun toPound(): Quanity {
        return when (unit) {
            Unit.OUNCE -> Quanity(amount / 16, Unit.POUND)
            Unit.POUND -> this
            Unit.GRAM -> Quanity(amount / 454, Unit.POUND)
            Unit.KILOGRAM -> Quanity(amount * 2.205, Unit.POUND)
            else -> this
        }
    }

    fun toGram(): Quanity {
        return when (unit) {
            Unit.OUNCE -> Quanity(amount * 28, Unit.GRAM)
            Unit.POUND -> Quanity(amount * 454, Unit.GRAM)
            Unit.GRAM -> this
            Unit.KILOGRAM -> Quanity(amount * 1000, Unit.GRAM)
            else -> this
        }
    }

    fun toKilogram(): Quanity {
        return when (unit) {
            Unit.OUNCE -> Quanity(amount / 35, Unit.KILOGRAM)
            Unit.POUND -> Quanity(amount / 2.205, Unit.KILOGRAM)
            Unit.GRAM -> Quanity(amount * 1000, Unit.KILOGRAM)
            else -> this
        }
    }
}