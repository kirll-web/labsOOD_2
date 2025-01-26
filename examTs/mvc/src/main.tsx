import {Calculator} from './view/view';
import {createRoot} from "react-dom/client";

const root = document.getElementById("root");

if (root) {
    createRoot(root).render(
        <div>
            <div style={{display: "flex", gap: 10}}>
                <Calculator/>
            </div>
        </div>
    );
}
