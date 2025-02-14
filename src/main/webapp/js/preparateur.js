document.addEventListener("DOMContentLoaded", function () {
    const confirmerBtns = document.querySelectorAll(".confirmer-btn");
    const finaliserBtn = document.getElementById("finaliser-btn");
    let confirmedCount = 0;

    confirmerBtns.forEach(btn => {
        btn.addEventListener("click", function () {
            this.disabled = true;
            this.style.backgroundColor = "gray";
            confirmedCount++;

            if (confirmedCount === confirmerBtns.length) {
                finaliserBtn.disabled = false;
            }
        });
    });

    finaliserBtn.addEventListener("click", function () {
        window.location.href = "finaliserCommande?id=" + new URLSearchParams(window.location.search).get("id");
    });
});
