<%--
  Created by IntelliJ IDEA.
  User: Gu HJ
  Date: 2025/2/25
  Time: 11:20
  To change this template use File | Settings | File Templates.
--%>
<%-- course.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>mes course</title>
    <style>
        .course-container {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 20px;
            padding: 20px;
        }
        .course-card {
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 15px;
            background: #fff;
        }
        .product-item {
            margin: 10px 0;
            padding: 10px;
            border-bottom: 1px solid #eee;
        }
    </style>
</head>
<body>
<h1>mes courses</h1>
<div id="courseContainer" class="course-container"></div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        fetch('${pageContext.request.contextPath}/course')
            .then(response => {
                if (!response.ok) throw new Error('网络响应异常');
                return response.json();
            })
            .then(courses => {
                const container = document.getElementById('courseContainer');
                container.innerHTML = courses.map(course => `
                        <div class="course-card">
                            <h3>购物车 #${course.idCourse}</h3>
                            <div class="products-list">
                                ${course.ajouts.map(ajout => `
                                    <div class="product-item">
                                        <p>商品：${ajout.produit.nomProduit}</p>
                                        <p>数量：${ajout.nombre}</p>
                                        <p>单价：€${ajout.produit.prixUnit.toFixed(2)}</p>
                                        ${ajout.produit.promotion > 0 ? `
                                            <p class="promo">促销价：€${ajout.produit.getPrixApresPromotion().toFixed(2)}</p>
                                        ` : ''}
                                    </div>
                                `).join('')}
                            </div>
                        </div>
                    `).join('');
            })
            .catch(error => {
                console.error('数据加载失败:', error);
                alert('无法加载course');
            });
    });
</script>
</body>
</html>
