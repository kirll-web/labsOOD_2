type IEvaluatorListener = (
  firstValue: number,
  secondValue: number,
  result: number
) => void;

export type IEvaluationStrategy = {
  execute: (firstValue: number, secondValue: number) => number;
};

export class StrategySum implements IEvaluationStrategy {
  execute(firstValue: number, secondValue: number): number {
    return firstValue + secondValue;
  }
}

export interface ICalculatorModel {
  updateFirstValue(firstValue: number): void;
  updateSecondValue(secondValue: number): void;
  registerListener(listener: IEvaluatorListener): void;
  unregisterListener(listener: IEvaluatorListener): void;
}

export class CalculatorModel implements ICalculatorModel {
  private m_firstValue = 87;
  private m_secondValue = 0;
  private m_result = 0;
  private m_evaluationStrategy: IEvaluationStrategy;
  private listeners: IEvaluatorListener[] = [];

  constructor(strategy: IEvaluationStrategy) {
    this.m_evaluationStrategy = strategy;
  }

  registerListener(listener: IEvaluatorListener): void {
    this.listeners.push(listener);
  }

  unregisterListener(listener: IEvaluatorListener): void {
    this.listeners = this.listeners.filter((l) => l !== listener);
  }

  setStrategy(strategy: IEvaluationStrategy): void {
    this.m_evaluationStrategy = strategy;
  }

  update(firstValue: number, secondValue: number): void {
    this.m_firstValue = firstValue;
    this.m_secondValue = secondValue;

    this.m_result = this.m_evaluationStrategy.execute(firstValue, secondValue);
    this.notifyListeners();
  }

  updateFirstValue(firstValue: number) {
    this.m_firstValue = firstValue;
    this.m_result = this.m_evaluationStrategy.execute(
      this.m_firstValue,
      this.m_secondValue
    );
    this.notifyListeners();
  }
  updateSecondValue(secondValue: number) {
    this.m_secondValue = secondValue;
    this.m_result = this.m_evaluationStrategy.execute(
      this.m_firstValue,
      this.m_secondValue
    );
    this.notifyListeners();
  }

  getResult(): number {
    return this.m_result;
  }

  private notifyListeners(): void {
    for (const listener of this.listeners) {
      listener(this.m_firstValue, this.m_secondValue, this.m_result);
    }
  }
}
