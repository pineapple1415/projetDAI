<%--
  Created by IntelliJ IDEA.
  User: rachi
  Date: 27/02/2025
  Time: 23:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Choisir votre créneau de retrait</title>
    <style>
        .container {
            width: 50%;
            margin: auto;
            text-align: center;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 10px;
            margin-top: 50px;
        }
        .slot {
            display: block;
            padding: 10px;
            margin: 10px auto;
            width: 70%;
            background: #f9f9f9;
            border: 1px solid #ccc;
            border-radius: 5px;
            cursor: pointer;
            transition: background 0.3s;
        }
        .slot:hover {
            background: #e9e9e9;
        }
        .slot input {
            display: none;
        }
        .slot.selected {
            background: #4CAF50;
            color: white;
            border: 2px solid #388E3C;
        }
        button {
            padding: 10px 20px;
            background: #008CBA;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-top: 20px;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Choisissez votre créneau de retrait</h2>
    <!-- choixCreneauRetrait.jsp -->
    <form action="creneau" method="post">
        <input type="hidden" name="idCommande" value="${idCommande}">

        <!-- Date avec valeur pré-remplie -->
        <label for="date">Date :</label>
        <input type="date" id="date" name="date"
               value="${currentDate}" required>

        <!-- Créneaux avec sélection actuelle -->
        <div id="slots">
            <label class="slot ${currentTimeSlot == '09:00-10:00' ? 'selected' : ''}">
                <input type="radio" name="timeSlot" value="09:00-10:00"
                ${currentTimeSlot == '09:00-10:00' ? 'checked' : ''} required>
                09:00 - 10:00
            </label>

            <label class="slot ${currentTimeSlot == '10:00-11:00' ? 'selected' : ''}">
                <input type="radio" name="timeSlot" value="10:00-11:00"
                ${currentTimeSlot == '10:00-11:00' ? 'checked' : ''}>
                10:00 - 11:00
            </label>
            <!-- Ajouter les autres créneaux de la même façon -->
            <label class="slot ${currentTimeSlot == '11:00-12:00' ? 'selected' : ''}">
                <input type="radio" name="timeSlot" value="10:00-11:00"
                ${currentTimeSlot == '11:00-12:00' ? 'checked' : ''}>
                11:00 - 12:00
            </label>
            <label class="slot ${currentTimeSlot == '14:00-15:00' ? 'selected' : ''}">
                <input type="radio" name="timeSlot" value="10:00-11:00"
                ${currentTimeSlot == '14:00-15:00' ? 'checked' : ''}>
                14:00 - 15:00
            </label>
            <label class="slot ${currentTimeSlot == '15:00-16:00' ? 'selected' : ''}">
                <input type="radio" name="timeSlot" value="10:00-11:00"
                ${currentTimeSlot == '15:00-16:00' ? 'checked' : ''}>
                15:00 - 16:00
            </label>
        </div>

        <button type="submit">${empty currentTimeSlot ? 'Réserver' : 'Modifier'} le créneau</button>
    </form>
</div>

<script>
    // Ajout d'un effet visuel pour la sélection des créneaux
    document.querySelectorAll(".slot").forEach(slot => {
        slot.addEventListener("click", function() {
            document.querySelectorAll(".slot").forEach(s => s.classList.remove("selected"));
            this.classList.add("selected");
            this.querySelector("input").checked = true;
        });
    });
</script>
</body>
</html>

