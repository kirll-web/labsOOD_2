
export class CalculatorModel {
  private num1: number = 0;
  private num2: number = 0;
  private sum: number = 0;
  private subscribers: (() => void)[] = [];

  setNum1(value: string) {
    this.num1 = Number(value) || 0;
    this.calculateSum();
    this.notify();
  }

  setNum2(value: string) {
    this.num2 = Number(value) || 0;
    this.calculateSum();
    this.notify();
  }

  private calculateSum() {
    this.sum = this.num1 + this.num2;
  }

  getSum() {
    return this.sum;
  }

  subscribe(callback: () => void) {
    this.subscribers.push(callback);
  }

  private notify() {
    this.subscribers.forEach(callback => callback());
  }
}