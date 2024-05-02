function showQRCode(fileId, sessionId, event) {
  console.log(`File ID: ${fileId}, Session ID: ${sessionId}`); // Проверка значений аргументов

  event.preventDefault();

  const downloadUrl = `/api/files/${fileId}?session=${sessionId}`;
  const qrCodeContainer = document.getElementById(`qrCodeContainer-${fileId}`);

  if (qrCodeContainer) {
    qrCodeContainer.innerHTML = ""; // Очищаем содержимое контейнера
    const qr = new QRious({
      element: qrCodeContainer,
      value: downloadUrl,
      size: 400,
      foreground: 'black',
      background: 'white'
    });

    // Показываем контейнер QR-кода
    qrCodeContainer.style.display = 'block';

    // Показываем модальное окно
    const modal = document.getElementById('myModal');
    modal.style.display = 'block';
  } else {
    console.error(`Контейнер qrCodeContainer-${fileId} не найден`);
  }
}

function closeModal() {
  const modal = document.getElementById('myModal');
  modal.style.display = 'none';
}