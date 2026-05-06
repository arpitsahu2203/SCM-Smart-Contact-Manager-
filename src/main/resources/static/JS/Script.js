// Initialize after DOM is ready to avoid querying elements too early
document.addEventListener("DOMContentLoaded", () => {
    console.log("Script Loaded");

    let currentTheme = getTheme();
    updateButton(currentTheme);
    setupToggle();

    function setupToggle() {
        const button = document.getElementById("themeToggleButton");
        if (!button) return; // nothing to do
        button.addEventListener("click", () => {
            currentTheme = currentTheme === "dark" ? "light" : "dark";

            document.documentElement.classList.remove("light", "dark");
            document.documentElement.classList.add(currentTheme);

            setTheme(currentTheme);
            updateButton(currentTheme);
        });
    }

    function updateButton(theme) {
        const button = document.getElementById("themeToggleButton");
        if (!button) return;
        const icon = button.querySelector("i");
        const text = button.querySelector("span");
        if (icon) {
            icon.className = theme === "dark" ? "fa-solid fa-moon" : "fa-solid fa-sun";
        }
        if (text) {
            text.textContent = theme === "dark" ? "Dark" : "Light";
        }
    }

    function setTheme(theme) {
        try { localStorage.setItem("theme", theme); } catch (e) { /* ignore */ }
    }

    function getTheme() {
        try {
            return localStorage.getItem("theme") ||
                (window.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light");
        } catch (e) {
            return window.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light";
        }
    }
});