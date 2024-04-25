function deleteSessionAndRedirect() {
    const sessionId = document.getElementById('sessionId').value;

    // Отправить запрос на удаление сессии
    fetch('/api/sessions/' + sessionId, {
        method: 'DELETE',
    })
        .then(response => {
            if (response.ok) {
                window.location.replace('/');
            } else {
                console.error('Ошибка удаления сессии:', response.status);
            }
        })
        .catch(error => {
            console.error('Произошла ошибка:', error);
        });
}
