const xhr = new XMLHttpRequest();

xhr.onload = function() {
    if (xhr.status === 200) {
        const content = document.getElementById('head_id');
        content.innerHTML = xhr.responseText;
    } else {
        console.error('Something went wrong!');
    }
};

xhr.open('GET', '../static/html/head.html', true);
xhr.send();

const xhr = new XMLHttpRequest();

xhr.onload = function() {
    if (xhr.status === 200) {
        const content = document.getElementById('login_info');
        content.innerHTML = xhr.responseText;
    } else {
        console.error('Something went wrong!');
    }
};

xhr.open('GET', '../static/html/head.html', true);
xhr.send();
