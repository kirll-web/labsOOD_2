import { createRoot } from "react-dom/client";
import { CalculatorModel, StrategySum } from "./model/model";
import { SummerView1 } from "./view/view";
import { CalculatorPresenter } from "./presenter/presenter";

const root = document.getElementById("root");

const evaluationStrategy = new StrategySum();
const calculatorModel = new CalculatorModel(evaluationStrategy);
const presenter = new CalculatorPresenter(calculatorModel);

if (root) {
  createRoot(root).render(
    <div>
      <div style={{ display: "flex", gap: 10 }}>
        <SummerView1 presenter={presenter} />
      </div>
    </div>
  );
}
