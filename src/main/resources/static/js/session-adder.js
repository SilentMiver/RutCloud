function createSession() {
    fetch('/api/sessions', {
        method: 'POST'
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error('Failed to create session');
        })
        .then(data => {
            window.location.href = '/send-files?session=' + data.id;
        })
        .catch(error => {
            console.error('Error creating session:', error);
        });
}