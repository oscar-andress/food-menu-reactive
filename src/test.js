const fs = require('fs');

const stream = fs.createWriteStream('datos_prueba.ndjson');

// Vamos a generar 10,000 registros
for (let i = 1; i <= 10000; i++) {
    const registro = {
        menuTitle: `Menu Automatizado ${i}`,
        menuDescription: `Descripción del menú número ${i}`
    };
    // Escribe la línea sin comas al final y con salto de línea
    stream.write(JSON.stringify(registro) + '\n');
}

stream.end();
console.log("Archivo generado con éxito.");