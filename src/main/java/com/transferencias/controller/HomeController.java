package com.transferencias.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HomeController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public Mono<String> home() {
        String apiUrl = System.getenv().getOrDefault("RAILWAY_PUBLIC_DOMAIN", "localhost:8080");
        String fullApiUrl = apiUrl.contains("localhost") ? "http://" + apiUrl : "https://" + apiUrl;

        return Mono.just("""
                    <!DOCTYPE html>
                    <html lang="es">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Sistema de Transferencias Bancarias</title>
                        <style>
                            * {
                                margin: 0;
                                padding: 0;
                                box-sizing: border-box;
                            }
                
                            body {
                                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                                min-height: 100vh;
                                padding: 20px;
                            }
                
                            .container {
                                max-width: 1200px;
                                margin: 0 auto;
                            }
                
                            h1 {
                                text-align: center;
                                color: white;
                                margin-bottom: 30px;
                                font-size: 2.5rem;
                                text-shadow: 2px 2px 4px rgba(0,0,0,0.2);
                            }
                
                            .card {
                                background: white;
                                border-radius: 15px;
                                padding: 30px;
                                margin-bottom: 30px;
                                box-shadow: 0 10px 30px rgba(0,0,0,0.2);
                            }
                
                            .card h2 {
                                color: #667eea;
                                margin-bottom: 20px;
                                border-bottom: 3px solid #667eea;
                                padding-bottom: 10px;
                            }
                
                            .form-group {
                                margin-bottom: 20px;
                            }
                
                            label {
                                display: block;
                                margin-bottom: 8px;
                                color: #333;
                                font-weight: 600;
                            }
                
                            input, select, textarea {
                                width: 100%;
                                padding: 12px;
                                border: 2px solid #e0e0e0;
                                border-radius: 8px;
                                font-size: 16px;
                                transition: border-color 0.3s;
                            }
                
                            input:focus, select:focus, textarea:focus {
                                outline: none;
                                border-color: #667eea;
                            }
                
                            button {
                                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                                color: white;
                                border: none;
                                padding: 15px 30px;
                                border-radius: 8px;
                                font-size: 16px;
                                font-weight: 600;
                                cursor: pointer;
                                transition: transform 0.2s, box-shadow 0.2s;
                                width: 100%;
                            }
                
                            button:hover {
                                transform: translateY(-2px);
                                box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
                            }
                
                            button:active {
                                transform: translateY(0);
                            }
                
                            .accounts-grid {
                                display: grid;
                                grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
                                gap: 20px;
                                margin-top: 20px;
                            }
                
                            .account-card {
                                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                                color: white;
                                padding: 20px;
                                border-radius: 12px;
                                box-shadow: 0 5px 15px rgba(0,0,0,0.1);
                            }
                
                            .account-card h3 {
                                margin-bottom: 10px;
                                font-size: 1.2rem;
                            }
                
                            .account-card p {
                                margin: 5px 0;
                                opacity: 0.9;
                            }
                
                            .balance {
                                font-size: 2rem;
                                font-weight: bold;
                                margin: 15px 0;
                            }
                
                            .transfer-list {
                                margin-top: 20px;
                            }
                
                            .transfer-item {
                                background: #f8f9fa;
                                padding: 15px;
                                margin-bottom: 10px;
                                border-radius: 8px;
                                border-left: 4px solid #667eea;
                            }
                
                            .transfer-item.completed {
                                border-left-color: #28a745;
                            }
                
                            .transfer-item.failed {
                                border-left-color: #dc3545;
                            }
                
                            .status {
                                display: inline-block;
                                padding: 5px 15px;
                                border-radius: 20px;
                                font-size: 12px;
                                font-weight: 600;
                                text-transform: uppercase;
                            }
                
                            .status.completed {
                                background: #d4edda;
                                color: #155724;
                            }
                
                            .status.pending {
                                background: #fff3cd;
                                color: #856404;
                            }
                
                            .status.failed {
                                background: #f8d7da;
                                color: #721c24;
                            }
                
                            .alert {
                                padding: 15px;
                                border-radius: 8px;
                                margin-bottom: 20px;
                                font-weight: 500;
                            }
                
                            .alert-success {
                                background: #d4edda;
                                color: #155724;
                                border: 1px solid #c3e6cb;
                            }
                
                            .alert-error {
                                background: #f8d7da;
                                color: #721c24;
                                border: 1px solid #f5c6cb;
                            }
                
                            .loading {
                                text-align: center;
                                padding: 20px;
                                color: #667eea;
                            }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <h1>Sistema de Transferencias Bancarias</h1>
                
                            <div class="card">
                                <h2>Nueva Cuenta</h2>
                                <form id="accountForm">
                                    <div class="form-group">
                                        <label>Número de Cuenta</label>
                                        <input type="text" id="accountNumber" placeholder="Ej: ACC-004" required>
                                    </div>
                                    <div class="form-group">
                                        <label>Nombre del Titular</label>
                                        <input type="text" id="ownerName" placeholder="Ej: Pedro Rodríguez" required>
                                    </div>
                                    <div class="form-group">
                                        <label>Saldo Inicial (COP)</label>
                                        <input type="number" id="balance" placeholder="0.00" step="0.01" required>
                                    </div>
                                    <button type="submit">Crear Cuenta</button>
                                </form>
                            </div>
                
                            <div class="card">
                                <h2>Nueva Transferencia</h2>
                                <div id="transferAlert"></div>
                                <form id="transferForm">
                                    <div class="form-group">
                                        <label>Cuenta Origen</label>
                                        <select id="sourceAccount" required>
                                            <option value="">Seleccione una cuenta...</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label>Cuenta Destino</label>
                                        <select id="destinationAccount" required>
                                            <option value="">Seleccione una cuenta...</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label>Monto (COP)</label>
                                        <input type="number" id="amount" placeholder="0.00" step="0.01" required>
                                    </div>
                                    <div class="form-group">
                                        <label>Descripción</label>
                                        <textarea id="description" rows="3" placeholder="Motivo de la transferencia..."></textarea>
                                    </div>
                                    <button type="submit">Realizar Transferencia</button>
                                </form>
                            </div>
                
                            <div class="card">
                                <h2>Cuentas Activas</h2>
                                <div id="accountsList" class="accounts-grid">
                                    <div class="loading">Cargando cuentas...</div>
                                </div>
                            </div>
                
                            <div class="card">
                                <h2>Historial de Transferencias</h2>
                                <div id="transfersList" class="transfer-list">
                                    <div class="loading">Cargando transferencias...</div>
                                </div>
                            </div>
                        </div>
                
                        <script>
                            const API_URL = '/api';
                
                            // Cargar cuentas
                                        async function loadAccounts() {
                                        try {
                                            const response = await fetch(`${API_URL}/accounts`);
                                            const accounts = await response.json();
                
                                            const accountsList = document.getElementById('accountsList');
                                            accountsList.innerHTML = accounts.map(acc => `
                                                <div class="account-card">
                                                    <h3>${acc.accountNumber}</h3>
                                                    <p>${acc.ownerName}</p>
                                                    <div class="balance">COP ${acc.balance.toLocaleString('es-CO', {minimumFractionDigits: 2, maximumFractionDigits: 2})}</div>
                                                    <p>Creada: ${new Date(acc.createdAt).toLocaleDateString('es-CO')}</p>
                                                </div>
                                            `).join('');
                
                                            // 🔹 Guardar selección previa de los selects
                                            const sourceSelect = document.getElementById('sourceAccount');
                                            const destSelect = document.getElementById('destinationAccount');
                
                                            const savedSource = sourceSelect.value;
                                            const savedDest = destSelect.value;
                
                                            // 🔹 Crear opciones
                                            const options = accounts.map(acc =>
                                                `<option value="${acc.id}">${acc.accountNumber} - ${acc.ownerName} (COP ${acc.balance.toLocaleString('es-CO', {minimumFractionDigits: 2})})</option>`
                                            ).join('');
                
                                            // 🔹 Reemplazar opciones sin perder selección
                                            sourceSelect.innerHTML = '<option value="">Seleccione una cuenta...</option>' + options;
                                            destSelect.innerHTML = '<option value="">Seleccione una cuenta...</option>' + options;
                
                                            // 🔹 Restaurar selección anterior si todavía existe
                                            if (savedSource) sourceSelect.value = savedSource;
                                            if (savedDest) destSelect.value = savedDest;
                
                                            } catch (error) {
                                            console.error('Error al cargar cuentas:', error);
                                            }
                                        }
                
                
                                        // Cargar transferencias
                                        async function loadTransfers() {
                                            try {
                                                const response = await fetch(`${API_URL}/transfers`);
                                                const transfers = await response.json();
                
                                                const transfersList = document.getElementById('transfersList');
                                                transfersList.innerHTML = transfers.sort((a, b) =>
                                                    new Date(b.createdAt) - new Date(a.createdAt)
                                                ).map(transfer => `
                                                    <div class="transfer-item ${transfer.status.toLowerCase()}">
                                                        <strong>ID: ${transfer.id}</strong> |
                                                        Desde: ${transfer.sourceAccountId} → Hacia: ${transfer.destinationAccountId}
                                                        <br>
                                                        <strong>Monto: COP ${transfer.amount.toLocaleString('es-CO', {minimumFractionDigits: 2, maximumFractionDigits: 2})}</strong>
                                                        <br>
                                                        ${transfer.description || 'Sin descripción'}
                                                        <br>
                                                        <span class="status ${transfer.status.toLowerCase()}">${transfer.status}</span>
                                                        <small> - ${new Date(transfer.createdAt).toLocaleString('es-CO')}</small>
                                                    </div>
                                                `).join('');
                                            } catch (error) {
                                                console.error('Error al cargar transferencias:', error);
                                            }
                                        }
                
                                        // Crear cuenta
                                        document.getElementById('accountForm').addEventListener('submit', async (e) => {
                                            e.preventDefault();
                
                                            const account = {
                                                accountNumber: document.getElementById('accountNumber').value,
                                                ownerName: document.getElementById('ownerName').value,
                                                balance: parseFloat(document.getElementById('balance').value),
                                                currency: 'COP' // Siempre COP
                                            };
                
                                            try {
                                                const response = await fetch(`${API_URL}/accounts`, {
                                                    method: 'POST',
                                                    headers: { 'Content-Type': 'application/json' },
                                                    body: JSON.stringify(account)
                                                });
                
                                                if (response.ok) {
                                                    alert('Cuenta creada exitosamente');
                                                    e.target.reset();
                                                    loadAccounts();
                                                }
                                            } catch (error) {
                                                alert('Error al crear cuenta: ' + error.message);
                                            }
                                        });
                
                                        // Realizar transferencia
                                        document.getElementById('transferForm').addEventListener('submit', async (e) => {
                                            e.preventDefault();
                
                                            const transfer = {
                                                sourceAccountId: parseInt(document.getElementById('sourceAccount').value),
                                                destinationAccountId: parseInt(document.getElementById('destinationAccount').value),
                                                amount: parseFloat(document.getElementById('amount').value),
                                                description: document.getElementById('description').value
                                            };
                
                                            const alertDiv = document.getElementById('transferAlert');
                
                                            try {
                                                const response = await fetch(`${API_URL}/transfers`, {
                                                    method: 'POST',
                                                    headers: { 'Content-Type': 'application/json' },
                                                    body: JSON.stringify(transfer)
                                                });
                
                                                if (response.ok) {
                                                    alertDiv.innerHTML = '<div class="alert alert-success">✓ Transferencia realizada exitosamente</div>';
                                                    e.target.reset();
                                                    setTimeout(() => {
                                                        loadAccounts();
                                                        loadTransfers();
                                                        alertDiv.innerHTML = '';
                                                    }, 2000);
                                                } else {
                                                    const error = await response.text();
                                                    alertDiv.innerHTML = `<div class="alert alert-error">✗ Error: ${error}</div>`;
                                                }
                                            } catch (error) {
                                                alertDiv.innerHTML = `<div class="alert alert-error">✗ Error: ${error.message}</div>`;
                                            }
                                        });
                
                                        // Cargar datos iniciales
                                        loadAccounts();
                                        loadTransfers();
                
                                        // Recargar cada 5 segundos
                                        setInterval(() => {
                                            loadAccounts();
                                            loadTransfers();
                                        }, 5000);
                        </script>
                    </body>
                    </html>
                """);
    }
}