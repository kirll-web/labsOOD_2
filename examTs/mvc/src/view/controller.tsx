import {CalculatorModel} from "../model/model.tsx";

export class CalculatorController {
  private model: CalculatorModel;

  constructor(model: CalculatorModel) {
    this.model = model;
  }

  handleNum1Change(value: string) {
    this.model.setNum1(value);
  }

  handleNum2Change(value: string) {
    this.model.setNum2(value);
  }
}