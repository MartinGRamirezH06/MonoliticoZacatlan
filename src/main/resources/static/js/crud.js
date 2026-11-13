document.addEventListener('DOMContentLoaded', () => {
    const modal = document.getElementById('modalForm');
    if (!modal) return;
    const form = modal.querySelector('form');

    modal.addEventListener('show.bs.modal', (e) => {
        const btn = e.relatedTarget;
        if (!btn) return;

        form.reset();
        form.action = btn.dataset.action;
        modal.querySelector('.modal-title').textContent = btn.dataset.titulo || '';

        Object.keys(btn.dataset).forEach((k) => {
            if (!k.startsWith('campo')) return;
            let nombre = k.slice(5);
            nombre = nombre.charAt(0).toLowerCase() + nombre.slice(1);
            const campo = form.elements[nombre]
                || form.elements[nombre.replace(/([A-Z])/g, '_$1').toLowerCase()];
            if (campo) campo.value = btn.dataset[k] ?? '';
        });

        const pass = form.elements['password'];
        if (pass) pass.required = (btn.dataset.modo === 'crear');

        // El bloque solo se elige al crear; al editar se muestra pero no se puede cambiar
        const editando = btn.dataset.modo === 'editar';
        const bloque = form.elements['bloqueId'];
        if (bloque) bloque.disabled = editando;
        modal.querySelectorAll('[data-solo-editar]').forEach((el) => { el.hidden = !editando; });
    });
});
// Buscador: filtra las filas de la tabla mientras escribes
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.table-search input').forEach((input) => {
        const tabla = input.closest('.table-search')
            .parentElement.querySelector('table.admin-table');
        if (!tabla) return;

        const normalizar = (t) => t.toLowerCase()
            .normalize('NFD').replace(/[\u0300-\u036f]/g, '');

        input.addEventListener('input', () => {
            const q = normalizar(input.value.trim());
            tabla.querySelectorAll('tbody tr').forEach((fila) => {
                if (fila.querySelector('td[colspan]')) return;
                fila.hidden = q !== '' && !normalizar(fila.textContent).includes(q);
            });
        });
    });
});
document.addEventListener('DOMContentLoaded', () => {
    const producto = document.getElementById('productoVenta');
    const precio   = document.getElementById('precioVenta');
    const cantidad = document.getElementById('cantidadVenta');
    const total    = document.getElementById('totalVenta');
    if (!producto || !precio || !cantidad || !total) return;

    const fmt = new Intl.NumberFormat('es-MX', { style: 'currency', currency: 'MXN' });

    const calcular = () => {
        const p = parseFloat(precio.value);
        const c = parseInt(cantidad.value, 10);
        total.textContent = fmt.format(isNaN(p) || isNaN(c) ? 0 : p * c);
    };

    producto.addEventListener('change', () => {
        const min = producto.selectedOptions[0]?.dataset.min;
        if (min !== undefined) {
            // precio.value = min;
            precio.min = min;
        }
        calcular();
    });

    precio.addEventListener('input', calcular);
    cantidad.addEventListener('input', calcular);
    calcular();
});