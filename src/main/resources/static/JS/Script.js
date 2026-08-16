// Initialize after DOM is ready to avoid querying elements too early
document.addEventListener("DOMContentLoaded", () => {
    console.log("Script Loaded");

    let currentTheme = getTheme();
    updateButton(currentTheme);
    setupToggle();
    setupUserMenu();
    setupPageAnimations();
    setupShellAnimations();

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

    function setupUserMenu() {
        const button = document.getElementById("user-menu-button");
        const menu = document.getElementById("user-dropdown");

        if (!button || !menu) return;

        button.addEventListener("click", (event) => {
            event.stopPropagation();
            menu.classList.toggle("hidden");
            button.setAttribute("aria-expanded", menu.classList.contains("hidden") ? "false" : "true");
        });

        document.addEventListener("click", (event) => {
            if (!menu.classList.contains("hidden") && !menu.contains(event.target) && !button.contains(event.target)) {
                menu.classList.add("hidden");
                button.setAttribute("aria-expanded", "false");
            }
        });
    }

    function setupPageAnimations() {
        const page = document.getElementById("content");

        // Pages remain fully usable if the CDN is unavailable or the visitor
        // has requested reduced motion.
        if (!page || !window.gsap || window.matchMedia("(prefers-reduced-motion: reduce)").matches) {
            return;
        }

        const hero = page.querySelector("section:first-child");
        const heroText = hero ? hero.querySelectorAll("h1, h2, p, a, button") : [];
        const sections = page.querySelectorAll("section");
        const cards = page.querySelectorAll("article, details");

        // Run all entrance effects together. This avoids holding later page
        // blocks invisible while earlier elements finish their animation.
        const timeline = gsap.timeline({ defaults: { ease: "power3.out" } });

        if (heroText.length) {
            timeline.from(heroText, {
                autoAlpha: 0,
                y: 18,
                duration: 0.55,
                stagger: 0.06
            }, 0);
        }

        timeline.from(sections, {
            autoAlpha: 0,
            y: 18,
            duration: 0.55,
            stagger: 0.06
        }, 0);

        if (cards.length) {
            timeline.from(cards, {
                autoAlpha: 0,
                y: 14,
                duration: 0.45,
                stagger: 0.04
            }, 0.08);
        }

        // GSAP removes its inline values afterwards so CSS hover states remain
        // in control of the cards.
        timeline.set([heroText, sections, cards], {
            clearProps: "transform,opacity,visibility"
        });
    }

    function setupShellAnimations() {
        if (!window.gsap || window.matchMedia("(prefers-reduced-motion: reduce)").matches) {
            return;
        }

        const sidebarLinks = document.querySelectorAll("#sidebar-multi-level-sidebar a");
        const footer = document.querySelector("footer");

        if (sidebarLinks.length) {
            gsap.from(sidebarLinks, {
                autoAlpha: 0,
                x: -12,
                duration: 0.35,
                stagger: 0.045,
                ease: "power2.out",
                clearProps: "transform,opacity,visibility"
            });
        }

        if (footer) {
            gsap.from(footer, {
                autoAlpha: 0,
                y: 12,
                duration: 0.45,
                delay: 0.15,
                ease: "power2.out",
                clearProps: "transform,opacity,visibility"
            });
        }
    }
});
