console.log("Script Loaded");

let currentTheme = getTheme();
updateButton(currentTheme);
setupToggle();

function setupToggle() {
    const button = document.getElementById("themeToggleButton");
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
    const icon = button.querySelector("i");
    const text = button.querySelector("span");

    if (theme === "dark") {
        // Currently dark → show moon + "Dark"
        icon.className = "fa-solid fa-moon";
        text.textContent = "Dark";
    } else {
        // Currently light → show sun + "Light"
        icon.className = "fa-solid fa-sun";
        text.textContent = "Light";
    }
}

function setTheme(theme) {
    localStorage.setItem("theme", theme);
}

function getTheme() {
    return localStorage.getItem("theme") || "light";
}