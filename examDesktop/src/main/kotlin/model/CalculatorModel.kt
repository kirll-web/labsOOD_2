package model

import Strategy.IStrategy
import presenter.ICalculatorObserver


class CalculatorModel(
    private val strategy: IStrategy
): IModel {
    private var mFirstNumber = 0
    private var mSecondNumber = 0
    private var mResult = 0
    private val mObservers = mutableListOf<ICalculatorObserver>()

    override fun updateFirstNumber(number: Int) {
        mFirstNumber = number
        executeCalc()
    }

    override fun updateSecondNumber(number: Int) {
        mSecondNumber = number
        executeCalc()
    }

    override fun registerObserver(observer: ICalculatorObserver) {
        mObservers.add(observer)
    }

    override fun unegisterObserver(observer: ICalculatorObserver) {
        mObservers.remove(observer)
    }

    private fun executeCalc() {
        mResult = strategy.execute(mFirstNumber, mSecondNumber)
        notifyObservers()
    }

    private fun notifyObservers() {
        mObservers.forEach {
            it.update(mFirstNumber, mSecondNumber, mResult)
        }
    }

}