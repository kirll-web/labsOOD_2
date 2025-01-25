package model

import presenter.ICalculatorObserver


interface IModel {
    fun updateFirstNumber(number: Int)
    fun updateSecondNumber(number: Int)
    fun registerObserver(observer: ICalculatorObserver)
    fun unegisterObserver(observer: ICalculatorObserver)
}