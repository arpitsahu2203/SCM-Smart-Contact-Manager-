// Smart Contact Manager - Core Client Script & Interactive Micro-Engines
document.addEventListener("DOMContentLoaded", () => {
    // 1. Color Theme Management
    setupThemeEngine();

    // 2. Navigation & User Dropdown
    setupUserDropdown();

    // 3. Mobile Sidebar Navigation & Backdrop
    setupMobileSidebar();

    // 4. Contacts Directory Live Search, Filters & Modal
    setupContactsDirectory();

    // 5. Interactive Copy-to-Clipboard & Toast Engine
    setupClipboardAndToast();

    // 6. Animated Metric Counters
    setupAnimatedCounters();

    // 7. Registration Form Character Counter
    setupRegisterBioCounter();

    // 8. Keyboard Shortcuts (Ctrl+K or / to search)
    setupKeyboardShortcuts();

    // ==========================================
    // 1. THEME ENGINE
    // ==========================================
    function setupThemeEngine() {
        const toggleBtn = document.getElementById("themeToggleButton");
        if (!toggleBtn) return;

        let currentTheme = localStorage.getItem("theme") ||
            (window.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light");

        updateThemeIcon(currentTheme);

        toggleBtn.addEventListener("click", () => {
            currentTheme = currentTheme === "dark" ? "light" : "dark";
            document.documentElement.classList.remove("light", "dark");
            document.documentElement.classList.add(currentTheme);
            try { localStorage.setItem("theme", currentTheme); } catch (e) {}
            updateThemeIcon(currentTheme);
            showToast(currentTheme === "dark" ? "Dark mode activated" : "Light mode activated");
        });
    }

    function updateThemeIcon(theme) {
        const toggleBtn = document.getElementById("themeToggleButton");
        if (!toggleBtn) return;

        if (theme === "dark") {
            toggleBtn.innerHTML = `
                <svg class="w-4 h-4 text-amber-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z"/>
                </svg>
            `;
            toggleBtn.setAttribute("title", "Switch to light mode");
        } else {
            toggleBtn.innerHTML = `
                <svg class="w-4 h-4 text-slate-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z"/>
                </svg>
            `;
            toggleBtn.setAttribute("title", "Switch to dark mode");
        }
    }

    // ==========================================
    // 2. USER PROFILE DROPDOWN
    // ==========================================
    function setupUserDropdown() {
        const button = document.getElementById("user-menu-button");
        const menu = document.getElementById("user-dropdown");
        if (!button || !menu) return;

        button.addEventListener("click", (e) => {
            e.stopPropagation();
            menu.classList.toggle("hidden");
            const expanded = !menu.classList.contains("hidden");
            button.setAttribute("aria-expanded", expanded ? "true" : "false");
        });

        document.addEventListener("click", (e) => {
            if (!menu.classList.contains("hidden") && !menu.contains(e.target) && !button.contains(e.target)) {
                menu.classList.add("hidden");
                button.setAttribute("aria-expanded", "false");
            }
        });
    }

    // ==========================================
    // 3. MOBILE SIDEBAR TOGGLE & BACKDROP
    // ==========================================
    function setupMobileSidebar() {
        const toggleBtn = document.getElementById("sidebar-toggle-btn");
        const closeBtn = document.getElementById("sidebar-close-btn");
        const sidebar = document.getElementById("sidebar-multi-level-sidebar");
        const backdrop = document.getElementById("sidebar-backdrop");
        if (!sidebar) return;

        function openSidebar() {
            sidebar.classList.remove("-translate-x-full");
            if (backdrop) backdrop.classList.remove("hidden");
        }

        function closeSidebar() {
            sidebar.classList.add("-translate-x-full");
            if (backdrop) backdrop.classList.add("hidden");
        }

        if (toggleBtn) {
            toggleBtn.addEventListener("click", (e) => {
                e.stopPropagation();
                if (sidebar.classList.contains("-translate-x-full")) {
                    openSidebar();
                } else {
                    closeSidebar();
                }
            });
        }

        if (closeBtn) closeBtn.addEventListener("click", closeSidebar);
        if (backdrop) backdrop.addEventListener("click", closeSidebar);
    }

    // ==========================================
    // 4. CONTACTS DIRECTORY: LIVE FILTER & MODAL
    // ==========================================
    function setupContactsDirectory() {
        const searchInput = document.getElementById("contact-search-input") || document.getElementById("navbar-quick-search");
        const rows = document.querySelectorAll(".contact-item-row");
        const emptyState = document.getElementById("no-results-empty-state");
        const filterAllBtn = document.getElementById("filter-all-btn");
        const filterFavBtn = document.getElementById("filter-fav-btn");
        const liveCounter = document.getElementById("live-search-counter");

        let activeFilter = "all"; // "all" | "fav"

        function applyFilters() {
            if (!rows.length) return;
            const query = (searchInput ? searchInput.value.trim().toLowerCase() : "");
            let visibleCount = 0;

            rows.forEach(row => {
                const name = (row.getAttribute("data-name") || "").toLowerCase();
                const email = (row.getAttribute("data-email") || "").toLowerCase();
                const phone = (row.getAttribute("data-phone") || "").toLowerCase();
                const isFavorite = row.getAttribute("data-favorite") === "true";

                const matchesQuery = !query || name.includes(query) || email.includes(query) || phone.includes(query);
                const matchesTab = activeFilter === "all" || (activeFilter === "fav" && isFavorite);

                if (matchesQuery && matchesTab) {
                    row.style.display = "";
                    visibleCount++;
                } else {
                    row.style.display = "none";
                }
            });

            if (emptyState) {
                emptyState.classList.toggle("hidden", visibleCount > 0);
            }

            if (liveCounter) {
                liveCounter.textContent = query ? `Showing ${visibleCount} of ${rows.length}` : "";
            }
        }

        if (searchInput) {
            searchInput.addEventListener("input", applyFilters);
        }

        if (filterAllBtn && filterFavBtn) {
            filterAllBtn.addEventListener("click", () => {
                activeFilter = "all";
                filterAllBtn.className = "px-3.5 py-1.5 text-xs font-semibold rounded-xl bg-blue-600 text-white shadow-xs transition-all hover:scale-[1.02] active:scale-[0.98]";
                filterFavBtn.className = "px-3.5 py-1.5 text-xs font-semibold rounded-xl bg-slate-100 text-slate-600 hover:bg-slate-200 dark:bg-slate-800 dark:text-slate-300 dark:hover:bg-slate-700 transition-all hover:scale-[1.02] active:scale-[0.98]";
                applyFilters();
            });

            filterFavBtn.addEventListener("click", () => {
                activeFilter = "fav";
                filterFavBtn.className = "px-3.5 py-1.5 text-xs font-semibold rounded-xl bg-blue-600 text-white shadow-xs transition-all hover:scale-[1.02] active:scale-[0.98]";
                filterAllBtn.className = "px-3.5 py-1.5 text-xs font-semibold rounded-xl bg-slate-100 text-slate-600 hover:bg-slate-200 dark:bg-slate-800 dark:text-slate-300 dark:hover:bg-slate-700 transition-all hover:scale-[1.02] active:scale-[0.98]";
                applyFilters();
            });
        }

        // Quick View Modal
        const modal = document.getElementById("contact-profile-modal");
        const modalContainerCard = document.getElementById("modal-container-card");
        const closeModalBtn = document.getElementById("close-contact-modal-btn");

        if (modal) {
            document.querySelectorAll(".view-contact-btn").forEach(btn => {
                btn.addEventListener("click", (e) => {
                    e.preventDefault();
                    const row = btn.closest(".contact-item-row");
                    if (!row) return;

                    const name = row.getAttribute("data-name") || "Unknown";
                    const email = row.getAttribute("data-email") || "";
                    const phone = row.getAttribute("data-phone") || "";
                    const picture = row.getAttribute("data-picture") || "/Images/profile-svgrepo-com.svg";
                    const address = row.getAttribute("data-address") || "";
                    const desc = row.getAttribute("data-description") || "";
                    const isFav = row.getAttribute("data-favorite") === "true";
                    const linkedin = row.getAttribute("data-linkedin") || "";
                    const website = row.getAttribute("data-website") || "";
                    const twitter = row.getAttribute("data-twitter") || "";

                    // Populate fields
                    document.getElementById("modal-contact-name").textContent = name;
                    document.getElementById("modal-contact-email").textContent = email;
                    document.getElementById("modal-contact-phone").textContent = phone || "No phone provided";
                    document.getElementById("modal-contact-picture").src = picture;

                    const starEl = document.getElementById("modal-contact-star");
                    if (starEl) starEl.classList.toggle("hidden", !isFav);

                    const callBtn = document.getElementById("modal-call-btn");
                    if (callBtn) {
                        callBtn.href = phone ? "tel:" + phone : "#";
                        callBtn.classList.toggle("opacity-50", !phone);
                        callBtn.classList.toggle("pointer-events-none", !phone);
                    }

                    const emailBtn = document.getElementById("modal-email-btn");
                    if (emailBtn) {
                        emailBtn.href = email ? "mailto:" + email : "#";
                        emailBtn.classList.toggle("opacity-50", !email);
                        emailBtn.classList.toggle("pointer-events-none", !email);
                    }

                    // Address & Desc
                    const addrEl = document.getElementById("modal-contact-address");
                    const addrBox = document.getElementById("modal-address-container");
                    if (addrEl && addrBox) {
                        addrEl.textContent = address || "No address added";
                        addrBox.classList.toggle("hidden", !address);
                    }

                    const descEl = document.getElementById("modal-contact-desc");
                    const descBox = document.getElementById("modal-desc-container");
                    if (descEl && descBox) {
                        descEl.textContent = desc || "No notes available";
                        descBox.classList.toggle("hidden", !desc);
                    }

                    // Socials
                    const liBtn = document.getElementById("modal-linkedin-btn");
                    if (liBtn) {
                        liBtn.href = linkedin || "#";
                        liBtn.classList.toggle("hidden", !linkedin);
                    }

                    const webBtn = document.getElementById("modal-website-btn");
                    if (webBtn) {
                        webBtn.href = website || "#";
                        webBtn.classList.toggle("hidden", !website);
                    }

                    const twBtn = document.getElementById("modal-twitter-btn");
                    if (twBtn) {
                        twBtn.href = twitter || "#";
                        twBtn.classList.toggle("hidden", !twitter);
                    }

                    // Show modal with scale-in animation
                    modal.classList.remove("hidden");
                    setTimeout(() => {
                        if (modalContainerCard) {
                            modalContainerCard.classList.remove("scale-95");
                            modalContainerCard.classList.add("scale-100");
                        }
                    }, 10);
                });
            });

            function closeModal() {
                if (modalContainerCard) {
                    modalContainerCard.classList.remove("scale-100");
                    modalContainerCard.classList.add("scale-95");
                }
                setTimeout(() => {
                    modal.classList.add("hidden");
                }, 150);
            }

            if (closeModalBtn) {
                closeModalBtn.addEventListener("click", closeModal);
            }

            modal.addEventListener("click", (e) => {
                if (e.target === modal) {
                    closeModal();
                }
            });

            document.addEventListener("keydown", (e) => {
                if (e.key === "Escape" && !modal.classList.contains("hidden")) {
                    closeModal();
                }
            });
        }
    }

    // ==========================================
    // 5. CLIPBOARD & FLOATING TOAST SYSTEM
    // ==========================================
    function setupClipboardAndToast() {
        document.querySelectorAll(".copy-trigger").forEach(el => {
            el.addEventListener("click", (e) => {
                const textToCopy = el.getAttribute("data-copy");
                if (!textToCopy) return;

                navigator.clipboard.writeText(textToCopy).then(() => {
                    const badge = el.querySelector(".copy-badge");
                    if (badge) {
                        badge.classList.remove("hidden");
                        setTimeout(() => badge.classList.add("hidden"), 2000);
                    }
                    showToast(`Copied to clipboard: ${textToCopy}`);
                }).catch(() => {
                    showToast("Failed to copy");
                });
            });
        });
    }

    window.showToast = function(message) {
        let toast = document.getElementById("app-toast");
        let toastText = document.getElementById("app-toast-text");
        if (!toast) {
            toast = document.createElement("div");
            toast.id = "app-toast";
            toast.className = "fixed bottom-6 right-6 z-50 transition-all duration-300 translate-y-4 opacity-0";
            toast.innerHTML = `
                <div class="glass-panel px-4 py-3 rounded-xl shadow-2xl flex items-center gap-2.5 text-xs font-semibold text-slate-800 dark:text-slate-200 border border-slate-200 dark:border-slate-800">
                    <span class="flex h-2 w-2 rounded-full bg-emerald-500"></span>
                    <span id="app-toast-text"></span>
                </div>
            `;
            document.body.appendChild(toast);
            toastText = toast.querySelector("#app-toast-text");
        }

        toastText.textContent = message;
        toast.classList.remove("hidden");
        setTimeout(() => {
            toast.classList.remove("translate-y-4", "opacity-0");
            toast.classList.add("translate-y-0", "opacity-100");
        }, 10);

        setTimeout(() => {
            toast.classList.remove("translate-y-0", "opacity-100");
            toast.classList.add("translate-y-4", "opacity-0");
            setTimeout(() => toast.classList.add("hidden"), 300);
        }, 2800);
    };

    // ==========================================
    // 6. ANIMATED METRIC COUNTERS
    // ==========================================
    function setupAnimatedCounters() {
        const counters = document.querySelectorAll(".stat-counter");
        counters.forEach(counter => {
            const target = parseInt(counter.getAttribute("data-target"), 10);
            if (isNaN(target) || target === 0) return;

            let start = 0;
            const duration = 800; // ms
            const stepTime = Math.max(Math.floor(duration / target), 20);

            const timer = setInterval(() => {
                start += Math.ceil(target / (duration / stepTime));
                if (start >= target) {
                    counter.textContent = target;
                    clearInterval(timer);
                } else {
                    counter.textContent = start;
                }
            }, stepTime);
        });
    }

    // ==========================================
    // 7. REGISTRATION BIO CHARACTER COUNTER
    // ==========================================
    function setupRegisterBioCounter() {
        const aboutEl = document.getElementById("about");
        const countEl = document.getElementById("bio-char-count");
        if (aboutEl && countEl) {
            function updateBioCount() {
                const len = aboutEl.value.length;
                countEl.textContent = `${len} / 500`;
                if (len >= 450) {
                    countEl.classList.add("text-amber-500");
                } else {
                    countEl.classList.remove("text-amber-500");
                }
            }
            aboutEl.addEventListener("input", updateBioCount);
            updateBioCount();
        }
    }

    // ==========================================
    // 8. KEYBOARD SHORTCUTS
    // ==========================================
    function setupKeyboardShortcuts() {
        document.addEventListener("keydown", (e) => {
            // Ctrl+K or / to focus search
            if ((e.ctrlKey && e.key.toLowerCase() === "k") || (e.key === "/" && !["input", "textarea"].includes(document.activeElement.tagName.toLowerCase()))) {
                const search = document.getElementById("contact-search-input") || document.getElementById("navbar-quick-search");
                if (search) {
                    e.preventDefault();
                    search.focus();
                    search.select();
                }
            }
        });
    }
});
