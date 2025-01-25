import { Component } from "react";
import { ICalculatorPresenter } from "../presenter/presenter";

type SummerViewProps = {
  presenter: ICalculatorPresenter;
};

type SummerViewState = {
  firstValue: number;
  secondValue: number;
  result: number;
};

export interface SummerView {
  update(firstValue: number, secondValue: number, result: number): void;
}

class SummerView1
  extends Component<SummerViewProps, SummerViewState>
  implements SummerView
{
  private presenter: ICalculatorPresenter;
  state: SummerViewState = { firstValue: 0, secondValue: 0, result: 0 };
  constructor(props: SummerViewProps) {
    super(props);
    this.presenter = this.props.presenter;
  }

  override componentDidMount() {
    this.props.presenter.attachView(this);
  }

  override componentWillUnmount() {
    this.props.presenter.detachView(this);
  }

  update(firstValue: number, secondValue: number, result: number) {
    this.setState({ firstValue, secondValue, result });
  }

  override render() {
    return (
      <div style={{ display: "flex", flexDirection: "column", gap: "10px" }}>
        <input
          type="number"
          value={this.state.firstValue}
          onChange={(event) =>
            this.presenter.updateFirstNumber(event.target.value)
          }
          placeholder="Введите первое число"
        />
        <input
          type="number"
          value={this.state.secondValue}
          onChange={(event) =>
            this.presenter.updateSecondNumber(event.target.value)
          }
          placeholder="Введите второе число"
        />
        <div>Результат: {this.state.result}</div>
      </div>
    );
  }
}

export { SummerView1 };
