import {useEffect, useState} from "react";
import {CalculatorModel} from "../model/model.tsx";
import {CalculatorController} from "./controller.tsx";

const Calculator: React.FC = () => {
  const [model] = useState(() => new CalculatorModel());
  const [controller] = useState(() => new CalculatorController(model));
  const [sum, setSum] = useState(0);

  useEffect(() => {
    model.subscribe(() => setSum(model.getSum()));
  }, [model]);

  return (
      <div>
        <input
            type="number"
            defaultValue="0"
            onChange={(e) => controller.handleNum1Change(e.target.value)}
        />
        <input
            type="number"
            defaultValue="0"
            onChange={(e) => controller.handleNum2Change(e.target.value)}
        />
        <p>Sum: {sum}</p>
      </div>
  );
};


export {Calculator};