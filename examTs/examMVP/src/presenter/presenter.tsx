import { ICalculatorModel } from "../model/model";
import { SummerView } from "../view/view";

export interface ICalculatorPresenter {
  updateFirstNumber(num: string): void;
  updateSecondNumber(num: string): void;
  attachView(view: SummerView): void;
  detachView(view: SummerView): void;
}

type SummerViewState = {
  firstValue: number;
  secondValue: number;
  result: number;
};

export class CalculatorPresenter implements ICalculatorPresenter {
  private model: ICalculatorModel;
  private views: SummerView[] = []; // Список всех View, которые нужно обновить
  private state: SummerViewState;

  constructor(model: ICalculatorModel) {
    this.model = model;

    this.handle = this.handle.bind(this);

    this.model.registerListener(this.handle);

    this.state = {
      firstValue: 0,
      secondValue: 0,
      result: 0,
    };
  }

  updateFirstNumber(num: string) {
    this.model.updateFirstValue(Number(num));
  }

  updateSecondNumber(num: string) {
    this.model.updateSecondValue(Number(num));
  }

  // Добавление View в список для обновления
  attachView(view: SummerView): void {
    this.views.push(view);
  }

  // Удаление View из списка
  detachView(view: SummerView): void {
    this.views = this.views.filter((v) => v !== view);
  }

  // Обновление значений через Presenter
  handle(firstValue: number, secondValue: number, result: number) {
    this.state = { firstValue, secondValue, result };
    this.notifyViews();
  }

  // Уведомление всех представлений о необходимости обновления
  private notifyViews(): void {
    for (const view of this.views) {
      view.update(
        this.state.firstValue,
        this.state.secondValue,
        this.state.result
      );
    }
  }
}
