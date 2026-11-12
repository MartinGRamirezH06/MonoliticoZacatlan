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