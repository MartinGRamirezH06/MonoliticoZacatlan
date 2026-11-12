    -- =====================================================================
-- Datos de prueba para Sistema Zacatlan (reemplaza src/main/resources/data.sql)
-- Las fechas son relativas a CURDATE(): siempre hay ventas de hoy, de esta
-- semana, del mes y del mes anterior, sin importar cuando se arranque.
-- ddl-auto=create recrea las tablas en cada arranque, asi que no se duplican.
-- =====================================================================

-- ---------- BLOQUES (solo 3) ----------
INSERT INTO bloque (id, nombre, descripcion, status) VALUES
(1, 'Comida',       'Puestos de comida tipica y antojitos', true),
(2, 'Manualidades', 'Manualidades y souvenirs',             true),
(3, 'Textil',       'Ropa, rebozos y telas',                true);

-- ---------- VENDEDORES (contrasena maximo 10 caracteres) ----------
-- 1 = ADMIN. El 7 y el 8 estan inactivos.
INSERT INTO vendedor (id, nombre, apellido_paterno, apellido_materno, correo, telefono, rol, bloque_id, password, status) VALUES
(1, 'Admin',  'Zacatlan',   '',        'admin@zacatlan.gob.mx',    '2481234567', 'ADMIN',    1, 'admin123',  true),
(2, 'Juan',   'Ramirez',    'Hilario', 'vendedor@zacatlan.gob.mx', '5551917533', 'VENDEDOR', 1, 'vendedor1', true),
(3, 'Maria',  'Lopez',      'Cruz',    'maria@zacatlan.gob.mx',    '2481112233', 'VENDEDOR', 1, 'maria1234', true),
(4, 'Pedro',  'Sanchez',    'Diaz',    'pedro@zacatlan.gob.mx',    '2482223344', 'VENDEDOR', 2, 'pedro1234', true),
(5, 'Lucia',  'Hernandez',  'Mora',    'lucia@zacatlan.gob.mx',    '2483334455', 'VENDEDOR', 3, 'lucia1234', true),
(6, 'Carlos', 'Torres',     'Vega',    'carlos@zacatlan.gob.mx',   '2484445566', 'VENDEDOR', 1, 'carlos123', true),
(7, 'Rosa',   'Martinez',   'Pena',    'rosa@zacatlan.gob.mx',     '2485556677', 'VENDEDOR', 1, 'rosa12345', false),
(8, 'Luis',   'Perez',      'Soto',    'luis@zacatlan.gob.mx',     '2486667788', 'VENDEDOR', 2, 'luis12345', false);

-- ---------- PRODUCTOS (el 9 esta inactivo) ----------
INSERT INTO producto (id, nombre, precio_min, categoria, status) VALUES
(1, 'Pambazos',        30.00, 'Comida',       true),
(2, 'Tamales',         15.00, 'Comida',       true),
(3, 'Atole',           20.00, 'Comida',       true),
(4, 'Taza',            50.00, 'Manualidades', true),
(5, 'Llavero',         25.00, 'Manualidades', true),
(6, 'Camiseta',       200.00, 'Textil',       true),
(7, 'Rebozo',         350.00, 'Textil',       true),
(8, 'Manzana (kg)',    40.00, 'Comida',       true),
(9, 'Souvenir viejo',  10.00, 'Manualidades', false);

-- ---------- VENTAS ----------
-- (vendedor, producto, fecha, precio_unitario, cantidad, total=0 -> se calcula abajo, status)
INSERT INTO venta (vendedor_id, producto_id, fecha, precio_unitario, cantidad, total, status) VALUES
-- HOY (reporte diario)
(2, 1, CURDATE(), 30.00, 10, 0, true),
(2, 2, CURDATE(), 15.00,  8, 0, true),
(2, 3, CURDATE(), 22.00,  6, 0, true),
(3, 1, CURDATE(), 35.00,  4, 0, true),
(3, 3, CURDATE(), 20.00, 12, 0, true),
(4, 4, CURDATE(), 55.00,  3, 0, true),
(4, 5, CURDATE(), 25.00, 10, 0, true),
(5, 6, CURDATE(), 220.00, 2, 0, true),
(6, 8, CURDATE(), 40.00,  5, 0, true),
-- Hace 1 a 3 dias (misma semana)
(2, 1, CURDATE() - INTERVAL 1 DAY, 30.00, 12, 0, true),
(3, 2, CURDATE() - INTERVAL 1 DAY, 15.00, 20, 0, true),
(4, 4, CURDATE() - INTERVAL 1 DAY, 50.00,  4, 0, true),
(5, 7, CURDATE() - INTERVAL 1 DAY, 380.00, 1, 0, true),
(6, 8, CURDATE() - INTERVAL 1 DAY, 42.00,  6, 0, true),
(2, 3, CURDATE() - INTERVAL 2 DAY, 20.00,  9, 0, true),
(3, 1, CURDATE() - INTERVAL 2 DAY, 32.00,  7, 0, true),
(4, 5, CURDATE() - INTERVAL 2 DAY, 25.00, 15, 0, true),
(5, 6, CURDATE() - INTERVAL 2 DAY, 200.00, 3, 0, true),
(2, 1, CURDATE() - INTERVAL 3 DAY, 30.00,  8, 0, true),
(3, 2, CURDATE() - INTERVAL 3 DAY, 15.00, 14, 0, true),
(6, 8, CURDATE() - INTERVAL 3 DAY, 40.00,  8, 0, true),
(4, 4, CURDATE() - INTERVAL 3 DAY, 60.00,  2, 0, true),
-- Semana pasada
(2, 2, CURDATE() - INTERVAL 5 DAY, 15.00, 18, 0, true),
(3, 1, CURDATE() - INTERVAL 5 DAY, 30.00,  9, 0, true),
(4, 5, CURDATE() - INTERVAL 5 DAY, 28.00,  6, 0, true),
(5, 6, CURDATE() - INTERVAL 6 DAY, 210.00, 2, 0, true),
(5, 7, CURDATE() - INTERVAL 6 DAY, 350.00, 1, 0, true),
(6, 8, CURDATE() - INTERVAL 6 DAY, 40.00, 10, 0, true),
(2, 3, CURDATE() - INTERVAL 7 DAY, 20.00, 11, 0, true),
(3, 3, CURDATE() - INTERVAL 7 DAY, 20.00,  7, 0, true),
-- Principio del mes
(2, 1, CURDATE() - INTERVAL 9 DAY,  30.00, 13, 0, true),
(3, 2, CURDATE() - INTERVAL 10 DAY, 15.00, 16, 0, true),
(4, 4, CURDATE() - INTERVAL 10 DAY, 50.00,  5, 0, true),
(5, 6, CURDATE() - INTERVAL 11 DAY, 200.00, 4, 0, true),
(6, 8, CURDATE() - INTERVAL 11 DAY, 45.00,  7, 0, true),
-- Mes anterior
(2, 1, CURDATE() - INTERVAL 15 DAY, 30.00, 10, 0, true),
(3, 1, CURDATE() - INTERVAL 20 DAY, 33.00,  6, 0, true),
(4, 5, CURDATE() - INTERVAL 25 DAY, 25.00, 12, 0, true),
(5, 7, CURDATE() - INTERVAL 30 DAY, 360.00, 2, 0, true),
(6, 8, CURDATE() - INTERVAL 35 DAY, 40.00,  9, 0, true),
(2, 2, CURDATE() - INTERVAL 40 DAY, 15.00, 25, 0, true),
-- Historial de vendedores/producto inactivos (deben aparecer en reportes)
(7, 1, CURDATE() - INTERVAL 25 DAY, 30.00,  3, 0, true),
(8, 4, CURDATE() - INTERVAL 20 DAY, 50.00,  2, 0, true),
(4, 9, CURDATE() - INTERVAL 30 DAY, 10.00,  5, 0, true),
-- Venta DESACTIVADA (status=false): NO debe salir en ningun reporte
(2, 1, CURDATE(), 30.00, 99, 0, false);

-- total = precio * cantidad
UPDATE venta SET total = precio_unitario * cantidad;

-- ---------- BITACORA (acciones del admin) ----------
INSERT INTO bitacora (admin_id, entidad, entidad_id, accion, detalle, fecha) VALUES
(1, 'VENDEDOR', 7, 'DESACTIVAR', NULL,                                                      NOW() - INTERVAL 20 DAY),
(1, 'PRODUCTO', 9, 'DESACTIVAR', NULL,                                                      NOW() - INTERVAL 12 DAY),
(1, 'PRODUCTO', 6, 'EDITAR',     'Antes: Precio min: 180.00 | Despues: Precio min: 200.00', NOW() - INTERVAL 6 DAY),
(1, 'VENDEDOR', 8, 'DESACTIVAR', NULL,                                                      NOW() - INTERVAL 3 DAY),
(1, 'VENTA',    1, 'EDITAR',     'Antes: Cantidad: 9 | Despues: Cantidad: 10',              NOW() - INTERVAL 1 DAY);
