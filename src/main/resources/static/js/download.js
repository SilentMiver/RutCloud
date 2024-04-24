function downloadFile(fileId, sessionId) {
    const downloadUrl = '/api/files/' + fileId + '?session=' + sessionId;
    window.location.href = downloadUrl;
}