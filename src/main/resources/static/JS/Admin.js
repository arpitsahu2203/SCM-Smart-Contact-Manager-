// Admin / Add Contact Image Dropzone & Preview Handler
document.addEventListener("DOMContentLoaded", () => {
    const fileInput = document.querySelector("#imageFileInput");
    const previewImg = document.getElementById("uplaod_image_preview");
    const previewWrapper = document.getElementById("preview-wrapper");
    const emptyPrompt = document.getElementById("dropzone-empty-prompt");
    const removeBtn = document.getElementById("remove-preview-btn");
    const dropzone = document.querySelector(".dropzone-cyan");
    const bioTextarea = document.getElementById("description");
    const bioCounter = document.getElementById("contact-bio-count");

    // 1. Image Preview & Dropzone
    if (fileInput && previewImg) {
        function handleFile(file) {
            if (!file || !file.type.startsWith("image/")) return;

            const reader = new FileReader();
            reader.onload = function(e) {
                previewImg.src = e.target.result;
                if (previewWrapper) previewWrapper.classList.remove("hidden");
                if (emptyPrompt) emptyPrompt.classList.add("hidden");
            };
            reader.readAsDataURL(file);
        }

        fileInput.addEventListener("change", function(event) {
            const file = event.target.files[0];
            handleFile(file);
        });

        if (removeBtn) {
            removeBtn.addEventListener("click", (e) => {
                e.stopPropagation();
                fileInput.value = "";
                previewImg.src = "";
                if (previewWrapper) previewWrapper.classList.add("hidden");
                if (emptyPrompt) emptyPrompt.classList.remove("hidden");
            });
        }

        if (dropzone) {
            ["dragenter", "dragover"].forEach(eventName => {
                dropzone.addEventListener(eventName, (e) => {
                    e.preventDefault();
                    e.stopPropagation();
                    dropzone.classList.add("drag-over");
                }, false);
            });

            ["dragleave", "drop"].forEach(eventName => {
                dropzone.addEventListener(eventName, (e) => {
                    e.preventDefault();
                    e.stopPropagation();
                    dropzone.classList.remove("drag-over");
                }, false);
            });

            dropzone.addEventListener("drop", (e) => {
                const dt = e.dataTransfer;
                const files = dt.files;
                if (files && files.length > 0) {
                    fileInput.files = files;
                    handleFile(files[0]);
                }
            }, false);
        }
    }

    // 2. Real-time Bio Character Counter
    if (bioTextarea && bioCounter) {
        function updateCount() {
            const len = bioTextarea.value.length;
            bioCounter.textContent = `${len} / 500`;
            if (len >= 450) {
                bioCounter.classList.add("text-amber-500");
            } else {
                bioCounter.classList.remove("text-amber-500");
            }
        }
        bioTextarea.addEventListener("input", updateCount);
        updateCount();
    }
});