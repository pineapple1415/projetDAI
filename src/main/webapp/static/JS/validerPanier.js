document.addEventListener("DOMContentLoaded", function() {
    function loadCartItems() {
        fetch(`${window.location.origin}/ProjetDAI_war/panier`)
            .then(response => {
                console.log(response);
                if (!response.ok) throw new Error(`购物车加载失败: ${response.status}`);
                return response.json();
            })
            .then(cart => {
                console.log(cart);
                const tbody = document.getElementById("cartBody");
                let total = 0;

                tbody.innerHTML = cart.items.map(item => {
                    const itemTotal = item.prixUnit * item.quantity;
                    total += itemTotal;
                    return `<tr>
                    <td>${item.nomProduit}</td>
                    <td>${item.quantity}</td>
                    <td>€${item.prixUnit.toFixed(2)}</td>
                    <td>€${itemTotal.toFixed(2)}</td>
                </tr>`;
                }).join('');

                document.getElementById("totalPrice").textContent =
                    `Total : €${total.toFixed(2)}`;
            })
            .catch(error => {
                console.error('购物车加载错误:', error);
                alert('无法加载购物车内容');
            });
    }

    function loadMagasins() {
        fetch(`${window.location.origin}/ProjetDAI_war/getMagasins`)
            .then(response => {
                if (!response.ok) throw new Error(`HTTP错误! 状态码: ${response.status}`);
                return response.json();
            })
            .then(magasins => {
                const select = document.getElementById("magasin");
                select.innerHTML = '<option value="">choisir magasin</option>' +
                    magasins.map(m => `
                    <option value="${m.idMagasin}">
                        ${m.nomMagasin} - ${m.adresseMagasin}
                    </option>
                `).join('');
            })
            .catch(error => {
                console.error('加载商店失败:', error);
                alert('无法加载商店列表');
            });
    }

    loadMagasins();


    document.getElementById("storeForm")?.addEventListener("submit", function(e) {
        e.preventDefault();
        const magasinId = document.getElementById("magasin").value;

        fetch(`${window.location.origin}/ProjetDAI_war/validerPanier`, {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `magasin=${encodeURIComponent(magasinId)}`
        }).then(response => {
            if (response.redirected) {
                window.location.href = response.url;
            } else if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
        }).catch(error => {
            console.error("提交失败:", error);
            alert("订单提交失败，请稍后重试");
        });
    });
});

