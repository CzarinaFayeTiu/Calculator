package com.mobdeve.s12.tiu.czarina.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.mobdeve.s12.tiu.czarina.calculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    var binding:ActivityMainBinding? = null
    lateinit var computation: TextView
    lateinit var results: TextView
    var error = false
    var lastNumeric= false
    var lastDot= false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        computation = binding!!.tvComputation
        results = binding!!.tvResults
    }


    //add all pressed numbers to the computation line
    fun onDigit(view: View) {
        if (error) {
            //possible error like 7/0
            computation.text = (view as Button).text
            error = false
        } else {
            computation.append((view as Button).text)
        }
        /*
            This will signal if a decimal point can be placed
            on the computation line
         */
        lastNumeric = true
    }

    //add a decimal point to the computation line
    fun onDecimalPoint(view: View) {
        /*
            if there is a number before the decimal point, and
            if there is no error and
            if there is no other decimal in the same number (i.e. 7.9.8.6)

            then allow the decimal point to be appended on
            the computation line
         */
        if (lastNumeric && !error && !lastDot) {
            computation.append(".")
            //since the last is now a decimal point
            lastNumeric = false
            lastDot = true
        }
    }

    //add an operator to the computation line
    fun onOperator(view: View) {
        /*
            If there is a last number before the operator and
            If there is no error

            then append the operator to the computation line
         */
        if (lastNumeric && !error) {
            computation.append((view as Button).text)
            //after an operator so number next
            lastNumeric = false
            lastDot = false
        }
    }

    //c button for now us it to clear
    fun clear(view: View) {
        this.computation.text = ""
        lastNumeric = false
        error = false
        lastDot = false
    }

    //backspace a character
    fun delete(view: View)
    {
        var s = computation.length()
        if(s > 0)
            computation.text = computation.text.subSequence(0, s-1)
    }

    /*
        Calculate everything in the computation line
        uses Exp4j library for computation
     */
    fun equals(view: View) {
        /*
            If last number is
         */
        if (lastNumeric && !error) {
            // Read the expression
            val txt = computation.text.toString()
            // Create an Expression (A class from exp4j library)
            val expression = ExpressionBuilder(txt).build()
            try {
                // Calculate the result and display
                val result = expression.evaluate()
                computation.text = result.toString()
                lastDot = true // Result contains a dot
            } catch (ex: ArithmeticException) {
                // Display an error message
                computation.text = "Error"
                error = true
                lastNumeric = false
            }
        }
    }

}
