package com.swbg.mlivestreaming

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.swbg.mlivestreaming", appContext.packageName)
    }


    data class Person(var name: String, var age: Int)

    operator fun Int.plus(b: Person): Int {
        return this - b.age
    }

    @Test
    fun main() {

        val person1 = Person("A", 3)

        val testInt = 5

        println("testInt+person1=${testInt +person1}")
    }
//输出结果
//    testInt+person1=2

}
