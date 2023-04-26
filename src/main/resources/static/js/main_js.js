function showMessage(message) {
    alert(message);
}

function showPopup(message) {
// Создаем элемент для сообщения
    var popup = document.createElement('div');
    popup.className = 'popup';
    popup.textContent = message;

// Добавляем элемент на страницу
    document.body.appendChild(popup);

// Задаем стили для сообщения
    popup.style.position = 'fixed';
    popup.style.top = '50%';
    popup.style.left = '50%';
    popup.style.transform = 'translate(-50%, -50%)';
    popup.style.backgroundColor = '#fff';
    popup.style.padding = '10px';
    popup.style.border = '1px solid #000';

// Закрытие сообщения при клике на него
    popup.onclick = function() {
        document.body.removeChild(popup);
    };
}

// Находим кнопку, при нажатии на которую будем вызывать функцию
var btn = document.querySelector('.btn');

// Создаем обработчик события для кнопки
btn.onclick = function() {
    showPopup();
};