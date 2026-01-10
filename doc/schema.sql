create table if not exists rol (
	id bigint auto_increment not null primary key,
	nombre varchar(255) not null unique
);

insert into rol(nombre) values
('ADMIN'),
('DEV'),
('EMPLEADO')
on duplicate key update nombre = nombre;

create table if not exists empleado (
	id bigint auto_increment not null primary key,
	nombre varchar(255) not null,
	username varchar(255) not null unique,
	password varchar(255) not null,
	id_rol bigint not null,
	constraint fk_empleado_rol foreign key (id_rol) references rol(id)
);

insert into empleado(nombre, username, password, id_rol) values
('Sol', 'solpm', 'tinta2025!', 1),
('Andrea', 'andreapm', 'tinta2025!', 1),
('Ben', 'benpm', 'BenYMar2605!', 2),
('Anto', 'antopm', 'tinta2025!', 3),
('Jose', 'josepm', 'tinta2025!', 3),
('Myrna', 'myrnapm', 'tinta2025!', 3),
('Meli', 'melipm', 'tinta2025!', 3),
('Mari', 'maripm', 'tinta2025!', 3);

-- agendas
create table if not exists tipo_tapa_agenda (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_tapa_agenda(id, tipo) values
(1, 'TAPA DURA'),
(2, 'OTRA');

create table if not exists tipo_color_agenda (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_color_agenda(tipo) values
('BLANCO Y NEGRO'),
('A COLOR');

create table if not exists agenda (
    id bigint auto_increment not null primary key,
    medida varchar(255) not null,
    tipo_tapa_personalizada varchar(255) null,
    cantidad_hojas int not null,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 5000,
    informacion_adicional varchar(255) null,
    id_tipo_tapa_agenda bigint not null,
    id_tipo_color_agenda bigint not null,
    constraint fk_tipo_tapa_agenda foreign key(id_tipo_tapa_agenda) references tipo_tapa_agenda(id),
    constraint fk_tipo_color_agenda foreign key(id_tipo_color_agenda) references tipo_color_agenda(id)
);

create table if not exists plantilla_agenda (
    id bigint auto_increment not null primary key,
    precio int not null,
    cantidad_hojas int not null,
    id_tipo_tapa_agenda bigint not null,
    id_tipo_color_agenda bigint not null,
    constraint fk_plantilla_tipo_tapa_agenda foreign key(id_tipo_tapa_agenda) references tipo_tapa_agenda(id),
    constraint fk_plantilla_tipo_color_agenda foreign key(id_tipo_color_agenda) references tipo_color_agenda(id)
);

insert into plantilla_agenda(cantidad_hojas, id_tipo_tapa_agenda, id_tipo_color_agenda, precio) values
(90, 1, 1, 24850); -- 90 hojas, tapa dura, blanco y negro

-- anotadores
create table if not exists anotador (
    id bigint auto_increment not null primary key,
    medida varchar(255) not null,
    tipo_tapa varchar(255) not null,
    cantidad_hojas int not null,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    informacion_adicional varchar(255) null,
    cantidad int not null
);

-- carpetas con solapas
create table if not exists tipo_laminado_carpeta_solapa (
    id bigint auto_increment not null primary key,
    laminado varchar(255) not null
);

insert into tipo_laminado_carpeta_solapa(laminado) values
('BRILLANTE'),
('MATE'),
('NINGUNO');

create table if not exists tipo_faz_carpeta_solapa (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_faz_carpeta_solapa(tipo) values
('SIMPLE FAZ'),
('DOBLE FAZ');

create table if not exists cantidad_carpeta_solapa (
    id bigint auto_increment not null primary key,
    cantidad varchar(255) not null
);

insert into cantidad_carpeta_solapa(cantidad) values
('1-49'),
('50'),
('100'),
('150'),
('OTRA');

create table if not exists carpeta_solapa (
    id bigint auto_increment not null primary key,
    tipo_papel varchar(255) not null,
    cantidad_personalizada varchar(255) null,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 5000,
    precio int null,
    informacion_adicional varchar(255) null,
    id_tipo_laminado_carpeta_solapa bigint not null,
    id_tipo_faz_carpeta_solapa bigint not null,
    id_cantidad_carpeta_solapa bigint not null,
    constraint fk_tipo_laminado_carpeta_solapa foreign key(id_tipo_laminado_carpeta_solapa) references tipo_laminado_carpeta_solapa(id),
    constraint fk_tipo_faz_carpeta_solapa foreign key(id_tipo_faz_carpeta_solapa) references tipo_faz_carpeta_solapa(id),
    constraint fk_cantidad_carpeta_solapa foreign key(id_cantidad_carpeta_solapa) references cantidad_carpeta_solapa(id)
);

create table if not exists plantilla_carpeta_solapa (
    id bigint auto_increment not null primary key,
    precio int not null,
    id_tipo_laminado_carpeta_solapa bigint not null,
    id_tipo_faz_carpeta_solapa bigint not null,
    id_cantidad_carpeta_solapa bigint not null,
    constraint fk_plantilla_tipo_laminado_carpeta_solapa foreign key(id_tipo_laminado_carpeta_solapa) references tipo_laminado_carpeta_solapa(id),
    constraint fk_plantilla_tipo_faz_carpeta_solapa foreign key(id_tipo_faz_carpeta_solapa) references tipo_faz_carpeta_solapa(id),
    constraint fk_plantilla_cantidad_carpeta_solapa foreign key(id_cantidad_carpeta_solapa) references cantidad_carpeta_solapa(id)
);

insert into plantilla_carpeta_solapa(id_tipo_laminado_carpeta_solapa, id_tipo_faz_carpeta_solapa, id_cantidad_carpeta_solapa, precio) values
(3, 1, 1, 2850), -- sin laminar, simple, 1-49 -- precio por u.
(3, 2, 1, 2850), -- sin laminar, doble, 1-49 -- precio por u.
(3, 1, 2, 121000), -- sin laminar, simple, 50
(3, 2, 2, 121000), -- sin laminar, doble, 50
(3, 1, 3, 217800), -- sin laminar, simple, 100
(3, 2, 3, 217800), -- sin laminar, doble, 100
(3, 1, 4, 293700), -- sin laminar, simple, 150
(3, 2, 4, 293700); -- sin laminar, doble, 150

-- catálogos
create table if not exists tipo_laminado_catalogo (
    id bigint auto_increment not null primary key,
    laminado varchar(255) not null
);

insert into tipo_laminado_catalogo(laminado) values
('BRILLANTE'),
('MATE'),
('NINGUNO');

create table if not exists tipo_faz_catalogo (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_faz_catalogo(tipo) values
('SIMPLE FAZ'),
('DOBLE FAZ');

create table if not exists catalogo (
    id bigint auto_increment not null primary key,
    tipo_papel varchar(255) not null,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 5000,
    precio int null,
    informacion_adicional varchar(255) null,
    id_tipo_laminado_catalogo bigint not null,
    id_tipo_faz_catalogo bigint not null,
    constraint fk_tipo_laminado_catalogo foreign key(id_tipo_laminado_catalogo) references tipo_laminado_catalogo(id),
    constraint fk_tipo_faz_catalogo foreign key(id_tipo_faz_catalogo) references tipo_faz_catalogo(id)
);

-- cierra bolsas
create table if not exists tipo_troquelado_cierra_bolsas (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_troquelado_cierra_bolsas(tipo) values
('CUADRADO'),
('CIRCULAR'),
('POR EL CONTORNO'),
('CORTE INDIVIDUAL'),
('SIN TROQUELAR');

create table if not exists medida_cierra_bolsas (
    id bigint auto_increment not null primary key,
    medida varchar(255) not null
);

insert into medida_cierra_bolsas(medida) values
('8X4 CM'),
('OTRA');

create table if not exists cantidad_cierra_bolsas (
    id bigint auto_increment not null primary key,
    cantidad varchar(255) not null
);

insert into cantidad_cierra_bolsas(cantidad) values
('100'),
('200'),
('500'),
('OTRA');

create table if not exists cierra_bolsas (
    id bigint auto_increment not null primary key,
    medida_personalizada varchar(255) null,
    cantidad_personalizada varchar(255) null
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 2500,
    precio int null,
    informacion_adicional varchar(255) null,
    id_tipo_troquelado_cierra_bolsas bigint not null,
    id_medida_cierra_bolsas bigint not null,
    id_cantidad_cierra_bolsas bigint not null,
    constraint fk_tipo_troquelado_cierra_bolsas foreign key(id_tipo_troquelado_cierra_bolsas) references tipo_troquelado_cierra_bolsas(id),
    constraint fk_medida_cierra_bolsas foreign key(id_medida_cierra_bolsas) references medida_cierra_bolsas(id),
    constraint fk_cantidad_cierra_bolsas foreign key(id_cantidad_cierra_bolsas) references cantidad_cierra_bolsas(id)
);

create table if not exists plantilla_cierra_bolsas (
    id bigint auto_increment not null primary key,
    precio int not null,
    id_tipo_troquelado_cierra_bolsas bigint not null,
    id_medida_cierra_bolsas bigint not null,
    id_cantidad_cierra_bolsas bigint not null,
    constraint fk_plantilla_tipo_troquelado_cierra_bolsas foreign key(id_tipo_troquelado_cierra_bolsas) references tipo_troquelado_cierra_bolsas(id),
    constraint fk_plantilla_medida_cierra_bolsas foreign key(id_medida_cierra_bolsas) references medida_cierra_bolsas(id),
    constraint fk_plantilla_cantidad_cierra_bolsas foreign key(id_cantidad_cierra_bolsas) references cantidad_cierra_bolsas(id)
);

insert into plantilla_cierra_bolsas(id_tipo_troquelado_cierra_bolsas, id_medida_cierra_bolsas, id_cantidad_cierra_bolsas, precio) values
(4, 1, 1, 9850), -- corte individual, 8x4, 100
(4, 1, 2, 18600), -- corte individual 8x4, 200
(4, 1, 3, 43600); -- corte individual, 8x4, 500

-- cuadernos anillados
create table if not exists tipo_tapa_cuaderno_anillado (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_tapa_cuaderno_anillado(tipo) values
('TAPA DURA'),
('OTRA');

create table if not exists medida_cuaderno_anillado (
    id bigint auto_increment not null primary key,
    medida varchar(255) not null
);

insert into medida_cuaderno_anillado(medida) values
('A4'),
('A5'),
('OTRA');

create table if not exists cuaderno_anillado (
    id bigint auto_increment not null primary key,
    medida_personalizada varchar(255) null,
    tipo_tapa_personalizada varchar(255) null,
    cantidad_hojas int not null,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 5000,
    precio int null,
    informacion_adicional varchar(255) null,
    id_tipo_tapa_cuaderno_anillado bigint not null,
    id_medida_cuaderno_anillado bigint not null,
    constraint fk_tipo_tapa_cuaderno_anillado foreign key(id_tipo_tapa_cuaderno_anillado) references tipo_tapa_cuaderno_anillado(id),
    constraint fk_medida_cuaderno_anillado foreign key(id_medida_cuaderno_anillado) references medida_cuaderno_anillado(id)
);

create table if not exists plantilla_cuaderno_anillado (
    id bigint auto_increment not null primary key,
    precio int not null,
    cantidad_hojas int not null,
    id_tipo_tapa_cuaderno_anillado bigint not null,
    id_medida_cuaderno_anillado bigint not null,
    constraint fk_plantilla_tipo_tapa_cuaderno_anillado foreign key(id_tipo_tapa_cuaderno_anillado) references tipo_tapa_cuaderno_anillado(id),
    constraint fk_plantilla_medida_cuaderno_anillado foreign key(id_medida_cuaderno_anillado) references medida_cuaderno_anillado(id)
);

insert into plantilla_cuaderno_anillado(cantidad_hojas, id_tipo_tapa_cuaderno_anillado, id_medida_cuaderno_anillado, precio) values
(80, 1, 1, 28950), -- 80 hojas, tapa dura, a4
(80, 1, 2, 22650); -- 80 hojas, tapa dura, a5

-- combos
create table if not exists tipo_combo (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_combo(tipo) values
('OFICINA'),
('PROFESIONAL'),
('EMPRENDEDOR X100'),
('EMPRENDEDOR X200'),
('EMPRENDEDOR X500');

create table if not exists combo (
    id bigint auto_increment not null primary key,
    descripcion_combo varchar(1000) not null default '-',
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 5000,
    precio int null,
    informacion_adicional varchar(255) null,
    id_tipo_combo bigint not null,
    constraint fk_tipo_combo foreign key(id_tipo_combo) references tipo_combo(id)
);

create table if not exists plantilla_combo (
    id bigint auto_increment not null primary key,
    precio int not null,
    descripcion_combo varchar(1000) not null,
    id_tipo_combo bigint not null,
    constraint fk_plantilla_tipo_combo foreign key(id_tipo_combo) references tipo_combo(id)
);

insert into plantilla_combo(id_tipo_combo, descripcion_combo, precio) values
(3, '100 cierra bolsas (8x4 cm), 100 etiquetas colgantes (7x5 cm), 100 circulares troqueladas (5 cm)', 22500), -- emprendedor 100
(4, '200 cierra bolsas (8x4 cm), 200 etiquetas colgantes (7x5 cm), 200 circulares troqueladas (5 cm)', 38250), -- emprendedor 200
(5, '500 cierra bolsas (8x4 cm), 500 etiquetas colgamtes (7x5 cm), 500 circulares troqueladas (5 cm)', 78400), -- emprendedor 500
(2, '100 tarjetas simple faz, 100 sobres oficio inglés, 2 talonarios RP blanco y negro (13x18 cm)', 75800),    -- profesional
(1, '100 tarjetas simple faz, 100 sobres oficio inglés, 1 talonario A4 a color con membrete', 101300);         -- oficina

-- entradas
create table if not exists tipo_papel_entrada (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_papel_entrada(tipo) values
('OBRA 75 GRS'),
('ILUSTRACIÓN 150 GRS');

create table if not exists tipo_color_entrada (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_color_entrada(tipo) values
('BLANCO Y NEGRO'),
('A COLOR');

create table if not exists tipo_troquelado_entrada (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_troquelado_entrada(tipo) values
('SIN TROQUELAR'),
('TROQUELADO SIMPLE'),
('TROQUELADO DOBLE');

create table if not exists medida_entrada (
    id bigint auto_increment not null primary key,
    medida varchar(255) not null
);

insert into medida_entrada(medida) values
('17X6 CM'),
('OTRA');

create table if not exists cantidad_entrada (
    id bigint auto_increment not null primary key,
    cantidad varchar(255) not null
);

insert into cantidad_entrada(cantidad) values
('100'),
('150'),
('200'),
('300'),
('500'),
('1000'),
('OTRA');

create table if not exists numerado_entrada (
    id bigint auto_increment not null primary key,
    numerado varchar(255) not null
);

insert into numerado_entrada(numerado) values
('SIN NUMERADO'),
('NUMERADO SIMPLE'),
('NUMERADO DOBLE');

create table if not exists terminacion_entrada (
    id bigint auto_increment not null primary key,
    terminacion varchar(255) not null
);

insert into terminacion_entrada(terminacion) values
('NINGUNA'),
('CON BROCHE Y TAPA');

create table if not exists entrada (
    id bigint auto_increment not nul l primary key,
    medida_personalizada varchar(255) null,
    cantidad_personalizada varchar(255) null,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 5000,
    precio int null,
    informacion_adicional varchar(255) null,
    id_tipo_papel_entrada bigint not null,
    id_tipo_color_entrada bigint not null,
    id_tipo_troquelado_entrada bigint not null,
    id_medida_entrada bigint not null,
    id_cantidad_entrada bigint not null,
    id_numerado_entrada bigint not null,
    id_terminacion_entrada bigint not null,
    constraint fk_tipo_papel_entrada foreign key(id_tipo_papel_entrada) references tipo_papel_entrada(id),
    constraint fk_tipo_color_entrada foreign key(id_tipo_color_entrada) references tipo_color_entrada(id),
    constraint fk_tipo_troquelado_entrada foreign key(id_tipo_troquelado_entrada) references tipo_troquelado_entrada(id),
    constraint fk_medida_entrada foreign key(id_medida_entrada) references medida_entrada(id),
    constraint fk_cantidad_entrada foreign key(id_cantidad_entrada) references cantidad_entrada(id),
    constraint fk_numerado_entrada foreign key(id_numerado_entrada) references numerado_entrada(id),
    constraint fk_terminacion_entrada foreign key(id_terminacion_entrada) references terminacion_entrada(id)
);

create table if not exists plantilla_entrada (
    id bigint auto_increment not null primary key,
    precio int not null,
    id_tipo_papel_entrada bigint not null,
    id_tipo_color_entrada bigint not null,
    id_tipo_troquelado_entrada bigint not null,
    id_medida_entrada bigint not null,
    id_cantidad_entrada bigint not null,
    id_numerado_entrada bigint not null,
    id_terminacion_entrada bigint not null,
    constraint fk_plantilla_tipo_papel_entrada foreign key(id_tipo_papel_entrada) references tipo_papel_entrada(id),
    constraint fk_plantilla_tipo_color_entrada foreign key(id_tipo_color_entrada) references tipo_color_entrada(id),
    constraint fk_plantilla_tipo_troquelado_entrada foreign key(id_tipo_troquelado_entrada) references tipo_troquelado_entrada(id),
    constraint fk_plantilla_medida_entrada foreign key(id_medida_entrada) references medida_entrada(id),
    constraint fk_plantilla_cantidad_entrada foreign key(id_cantidad_entrada) references cantidad_entrada(id),
    constraint fk_plantilla_numerado_entrada foreign key(id_numerado_entrada) references numerado_entrada(id),
    constraint fk_plantilla_terminacion_entrada foreign key(id_terminacion_entrada) references terminacion_entrada(id)
);

insert into plantilla_entrada(id_tipo_papel_entrada, id_tipo_color_entrada, id_tipo_troquelado_entrada, id_medida_entrada, id_cantidad_entrada, id_numerado_entrada, id_terminacion_entrada, precio) values
(1, 2, 3, 1, 1, 3, 2, 16900),  -- obra 75, color, doble troquel, 17x6, 100 u, doble num, broche y tapa
(1, 2, 3, 1, 2, 3, 2, 23200),  -- obra 75, color, doble troquel, 17x6, 150 u, doble num, broche y tapa
(1, 2, 3, 1, 3, 3, 2, 29800),  -- obra 75, color, doble troquel, 17x6, 200 u, doble num, broche y tapa
(1, 2, 3, 1, 4, 3, 2, 44500),  -- obra 75, color, doble troquel, 17x6, 300 u, doble num, broche y tapa
(1, 2, 3, 1, 5, 3, 2, 73200),  -- obra 75, color, doble troquel, 17x6, 500 u, doble num, broche y tapa
(1, 2, 3, 1, 6, 3, 2, 138700), -- obra 75, color, doble troquel, 17x6, 1000 u, doble num, broche y tapa
(2, 2, 3, 1, 1, 3, 2, 29800),  -- ilu. 150, color, doble troquel, 17x6, 100 u, doble num, broche y tapa
(2, 2, 3, 1, 2, 3, 2, 43200),  -- ilu. 150, color, doble troquel, 17x6, 150 u, doble num, broche y tapa
(2, 2, 3, 1, 3, 3, 2, 58900),  -- ilu. 150, color, doble troquel, 17x6, 200 u, doble num, broche y tapa
(2, 2, 3, 1, 4, 3, 2, 82500),  -- ilu. 150, color, doble troquel, 17x6, 300 u, doble num, broche y tapa
(2, 2, 3, 1, 5, 3, 2, 138700), -- ilu. 150, color, doble troquel, 17x6, 500 u, doble num, broche y tapa
(2, 2, 3, 1, 6, 3, 2, 255000), -- ilu. 150, color, doble troquel, 17x6, 1000 u, doble num, broche y tapa
(1, 1, 3, 1, 1, 3, 2, 9200),   -- obra 75, byn, doble troquel, 17x6, 100 u, doble num, broche y tapa
(1, 1, 3, 1, 2, 3, 2, 13500),  -- obra 75, byn, doble troquel, 17x6, 150 u, doble num, broche y tapa
(1, 1, 3, 1, 3, 3, 2, 17800),  -- obra 75, byn, doble troquel, 17x6, 200 u, doble num, broche y tapa
(1, 1, 3, 1, 4, 3, 2, 24900),  -- obra 75, byn, doble troquel, 17x6, 300 u, doble num, broche y tapa
(1, 1, 3, 1, 5, 3, 2, 39800),  -- obra 75, byn, doble troquel, 17x6, 500 u, doble num, broche y tapa
(1, 1, 3, 1, 6, 3, 2, 75600);  -- obra 75, byn, doble troquel, 17x6, 1000 u, doble num, broche y tapa

-- etiquetas
create table if not exists tipo_papel_etiqueta (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_papel_etiqueta(tipo) values
('OPALINA 180 GRS'),
('OPALINA 210 GRS'),
('ILUSTRACIÓN 250 GRS'),
('KRAFT'),
('ILUSTRACIÓN 2250 GRS');

create table if not exists tipo_laminado_etiqueta (
    id bigint auto_increment not null primary key,
    laminado varchar(255) not null
);

insert into tipo_laminado_etiqueta(laminado) values
('BRILLANTE'),
('MATE'),
('NINGUNO');

create table if not exists tamanio_perforacion (
    id bigint auto_increment not null primary key,
    tamanio varchar(255) not null
);

insert into tamanio_perforacion(tamanio) values
('NINGUNA'),
('CHICA'),
('MEDIANA'),
('GRANDE');

create table if not exists tipo_faz_etiqueta (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_faz_etiqueta(tipo) values
('SIMPLE FAZ'),
('DOBLE FAZ');

create table if not exists cantidad_etiqueta (
    id bigint auto_increment not null primary key,
    cantidad varchar(255) not null
);

insert into cantidad_etiqueta(cantidad) values
('100'),
('200'),
('500'),
('1000'),
('OTRA');

create table if not exists medida_etiqueta (
    id bigint auto_increment not null primary key,
    medida varchar(255) not null
);

insert into medida_etiqueta(medida) values
('7X5 CM'),
('OTRA')

create table if not exists etiqueta (
    id bigint auto_increment not null primary key,
    medida_personalizada varchar(255) not null,
    cantidad_personalizada varchar(255) not null,
    con_perforacion_adicional tinyint(1) not null default 0,
    con_marca_adicional tinyint(1) not null default 0,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 3500,
    precio int null,
    informacion_adicional varchar(255) null,
    id_tipo_papel_etiqueta bigint not null,
    id_tipo_laminado_etiqueta bigint not null,
    id_tamanio_perforacion bigint null,
    id_tipo_faz_etiqueta bigint not null,
    id_cantidad_etiqueta bigint not null,
    id_medida_etiqueta bigint not null,
    constraint fk_tipo_papel_etiqueta foreign key(id_tipo_papel_etiqueta) references tipo_papel_etiqueta(id),
    constraint fk_tipo_laminado_etiqueta foreign key(id_tipo_laminado_etiqueta) references tipo_laminado_etiqueta(id),
    constraint fk_tamanio_perforacion foreign key(id_tamanio_perforacion) references tamanio_perforacion(id),
    constraint fk_tipo_faz_etiqueta foreign key(id_tipo_faz_etiqueta) references tipo_faz_etiqueta(id),
    constraint fk_cantidad_etiqueta foreign key(id_cantidad_etiqueta) references cantidad_etiqueta(id),
    constraint fk_medida_etiqueta foreign key(id_medida_etiqueta) references medida_etiqueta(id)
);

create table if not exists plantilla_etiqueta (
    id bigint auto_increment not null primary key,
    precio int not null,
    id_tipo_papel_etiqueta bigint not null,
    id_tipo_laminado_etiqueta bigint not null,
    id_tipo_faz_etiqueta bigint not null,
    id_cantidad_etiqueta bigint not null,
    id_medida_etiqueta bigint not null,
    constraint fk_plantilla_tipo_papel_etiqueta foreign key(id_tipo_papel_etiqueta) references tipo_papel_etiqueta(id),
    constraint fk_plantilla_tipo_laminado_etiqueta foreign key(id_tipo_laminado_etiqueta) references tipo_laminado_etiqueta(id),
    constraint fk_plantilla_tipo_faz_etiqueta foreign key(id_tipo_faz_etiqueta) references tipo_faz_etiqueta(id),
    constraint fk_plantilla_cantidad_etiqueta foreign key(id_cantidad_etiqueta) references cantidad_etiqueta(id),
    constraint fk_plantilla_medida_etiqueta foreign key(id_medida_etiqueta) references medida_etiqueta(id)
);

insert into plantilla_etiqueta(id_tipo_papel_etiqueta, id_tipo_laminado_etiqueta, id_tipo_faz_etiqueta, id_cantidad_etiqueta, id_medida_etiqueta, precio) values
(4, 3, 1, 1, 1, 9700),  -- kraft, sin laminar, simple, 100, 7x5
(4, 3, 1, 2, 1, 17050), -- kraft, sin laminar, simple, 200, 7x5
(4, 3, 1, 3, 1, 40150), -- kraft, sin laminar, simple, 500, 7x5
(4, 3, 1, 4, 1, 70100), -- kraft, sin laminar, simple, 1000, 7x5
(4, 3, 2, 1, 1, 11150), -- kraft, sin laminar, doble, 100, 7x5
(4, 3, 2, 2, 1, 19750), -- kraft, sin laminar, doble, 200, 7x5
(4, 3, 2, 3, 1, 46200), -- kraft, sin laminar, doble, 500, 7x5
(4, 3, 2, 4, 1, 88250), -- kraft, sin laminar, doble, 1000, 7x5
(5, 3, 1, 1, 1, 9700),  -- 220 grs, sin laminar, simple, 100, 7x5
(5, 3, 1, 2, 1, 17050), -- 220 grs, sin laminar, simple, 200, 7x5
(5, 3, 1, 3, 1, 40150), -- 220 grs, sin laminar, simple, 500, 7x5
(5, 3, 1, 4, 1, 70100), -- 220 grs, sin laminar, simple, 1000, 7x5
(5, 3, 2, 1, 1, 11150), -- 220 grs, sin laminar, doble, 100, 7x5
(5, 3, 2, 2, 1, 19750), -- 220 grs, sin laminar, doble, 200, 7x5
(5, 3, 2, 3, 1, 46200), -- 220 grs, sin laminar, doble, 500, 7x5
(5, 3, 2, 4, 1, 88250), -- 220 grs, sin laminar, doble, 1000, 7x5
(1, 3, 1, 1, 1, 7950),  -- opalina 180, sin laminar, simple, 100, 7x5
(1, 3, 1, 2, 1, 14100), -- opalina 180, sin laminar, simple, 200, 7x5
(1, 3, 1, 3, 1, 33200), -- opalina 180, sin laminar, simple, 500, 7x5
(1, 3, 1, 4, 1, 59250), -- opalina 180, sin laminar, simple, 1000, 7x5
(1, 3, 2, 1, 1, 9700),  -- opalina 180, sin laminar, doble, 100, 7x5
(1, 3, 2, 2, 1, 17150), -- opalina 180, sin laminar, doble, 200, 7x5
(1, 3, 2, 3, 1, 40150), -- opalina 180, sin laminar, doble, 500, 7x5
(1, 3, 2, 4, 1, 70100); -- opalina 180, sin laminar, doble, 1000, 7x5

-- flybanners
create table if not exists tipo_faz_flybanner (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_faz_flybanner(tipo) values
('SIMPLE FAZ'),
('DOBLE FAZ');

create table if not exists altura_flybanner (
    id bigint auto_increment not null primary key,
    altura varchar(255) not null
);

insert into altura_flybanner(altura) values
('240 CM'),
('190 CM');

create table if not exists bandera_flybanner (
    id bigint auto_increment not null primary key,
    bandera varchar(255) not null
);

insert into bandera_flybanner(bandera) values
('70X145 CM');

create table if not exists tipo_base_flybanner (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_base_flybanner(tipo) values
('SIN BASE'),
('BASE CRUZ'),
('ESTACA ECO');

create table if not exists flybanner (
    id bigint auto_increment not null primary key,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 5000,
    precio int null,
    informacion_adicional varchar(255) null,
    id_tipo_faz_flybanner bigint not null,
    id_altura_flybanner bigint not null,
    id_bandera_flybanner bigint not null,
    id_tipo_base_flybanner bigint not null,
    constraint fk_tipo_faz_flybanner foreign key(id_tipo_faz_flybanner) references tipo_faz_flybanner(id),
    constraint fk_altura_flybanner foreign key(id_altura_flybanner) references altura_flybanner(id),
    constraint fk_bandera_flybanner foreign key(id_bandera_flybanner) references bandera_flybanner(id),
    constraint fk_tipo_base_flybanner foreign key(id_tipo_base_flybanner) references tipo_base_flybanner(id)
);

create table if not exists plantilla_flybanner (
    id bigint auto_increment not null primary key,
    precio int not null,
    id_tipo_faz_flybanner bigint not null,
    id_altura_flybanner bigint not null,
    id_bandera_flybanner bigint not null,
    id_tipo_base_flybanner bigint not null,
    constraint fk_plantilla_tipo_faz_flybanner foreign key(id_tipo_faz_flybanner) references tipo_faz_flybanner(id),
    constraint fk_plantilla_altura_flybanner foreign key(id_altura_flybanner) references altura_flybanner(id),
    constraint fk_plantilla_bandera_flybanner foreign key(id_bandera_flybanner) references bandera_flybanner(id),
    constraint fk_plantilla_tipo_base_flybanner foreign key(id_tipo_base_flybanner) references tipo_base_flybanner(id)
);

insert into plantilla_flybanner(id_tipo_faz_flybanner, id_altura_flybanner, id_bandera_flybanner, id_tipo_base_flybanner, precio) values
(2, 1, 1, 1, 106200), -- doble, 240, 70x145, sin base
(2, 1, 1, 2, 134400), -- doble, 240, 70x145, base cruz
(2, 1, 1, 3, 123950), -- doble, 240, 70x145, estaca eco
(1, 1, 1, 1, 97000),  -- simple, 240, 70x145, sin base
(1, 1, 1, 2, 125200), -- simple, 240, 70x145, base cruz
(1, 1, 1, 3, 114750), -- simple, 240, 70x145, estaca eco
(2, 2, 1, 1, 88200),  -- doble, 190, 70x145, sin base
(2, 2, 1, 2, 116400), -- doble, 190, 70x145, base cruz
(2, 2, 1, 3, 105950), -- doble, 190, 70x145, estaca eco
(1, 2, 1, 1, 79000),  -- simple, 190, 70x145, sin base
(1, 2, 1, 2, 107200), -- simple, 190, 70x145, base cruz
(1, 2, 1, 3, 96750);  -- simple, 190, 70x145, estaca eco

-- folletos
create table if not exists tipo_color_folleto (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_color_folleto(tipo) values
('BLANCO Y NEGRO'),
('A COLOR');

create table if not exists tamanio_hoja_folleto (
    id bigint auto_increment not null primary key,
    tamanio varchar(255) not null
);

insert into tamanio_hoja_folleto(tamanio) values
('A4'),
('A5'),
('1/4 DE A4'),
('A3'),
('OFICIO');

create table if not exists cantidad_folleto (
    id bigint auto_increment not null primary key,
    cantidad varchar(255) not null
);

insert into cantidad_folleto(cantidad) values
('50'),
('100'),
('150'),
('200'),
('300'),
('400'),
('500'),
('1000'),
('OTRA');

create table if not exists tipo_papel_folleto (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_papel_folleto(tipo) values
('OBRA 75 GRS'),
('ILUSTRACIÓN 115 GRS'),
('ILUSTRACIÓN 150 GRS');

create table if not exists tipo_folleto (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_folleto(tipo) values
('COMÚN'),
('DÍPTICO'),
('TRÍPTICO');

create table if not exists tipo_faz_folleto (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_faz_folleto(tipo) values
('SIMPLE FAZ'),
('DOBLE FAZ');

create table if not exists folleto (
    id bigint auto_increment not null primary key,
    cantidad_personalizada varchar(255) null,
    con_plegado tinyint(1) not null default 0,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 5000,
    precio int null,
    informacion_adicional varchar(255) null,
    id_tipo_papel_folleto bigint not null,
    id_tipo_color_folleto bigint not null,
    id_tipo_faz_folleto bigint not null,
    id_tamanio_hoja_folleto bigint not null,
    id_tipo_folleto bigint not null,
    id_cantidad_folleto bigint not null,
    constraint fk_tipo_papel_folleto foreign key(id_tipo_papel_folleto) references tipo_papel_folleto(id),
    constraint fk_tipo_color_folleto foreign key(id_tipo_color_folleto) references tipo_color_folleto(id),
    constraint fk_tipo_faz_folleto foreign key(id_tipo_faz_folleto) references tipo_faz_folleto(id),
    constraint fk_tamanio_hoja_folleto foreign key(id_tamanio_hoja_folleto) references tamanio_hoja_folleto(id),
    constraint fk_tipo_folleto foreign key(id_tipo_folleto) references tipo_folleto(id),
    constraint fk_cantidad_folleto foreign key(id_cantidad_folleto) references cantidad_folleto(id)
);

create table if not exists plantilla_folleto (
    id bigint auto_increment not null primary key,
    precio int not null,
    id_tipo_papel_folleto bigint not null,
    id_tipo_color_folleto bigint not null,
    id_tipo_faz_folleto bigint not null,
    id_tamanio_hoja_folleto bigint not null,
    id_tipo_folleto bigint not null,
    id_cantidad_folleto bigint not null,
    constraint fk_plantilla_tipo_papel_folleto foreign key(id_tipo_papel_folleto) references tipo_papel_folleto(id),
    constraint fk_plantilla_tipo_color_folleto foreign key(id_tipo_color_folleto) references tipo_color_folleto(id),
    constraint fk_plantilla_tipo_faz_folleto foreign key(id_tipo_faz_folleto) references tipo_faz_folleto(id),
    constraint fk_plantilla_tamanio_hoja_folleto foreign key(id_tamanio_hoja_folleto) references tamanio_hoja_folleto(id),
    constraint fk_plantilla_tipo_folleto foreign key(id_tipo_folleto) references tipo_folleto(id),
    constraint fk_plantilla_cantidad_folleto foreign key(id_cantidad_folleto) references cantidad_folleto(id)
);

insert into plantilla_folleto(id_tipo_papel_folleto, id_tipo_color_folleto, id_tipo_faz_folleto, id_tamanio_hoja_folleto, id_tipo_folleto, id_cantidad_folleto, precio) values
(1, 1, 1, 3, 1, 2, 6400), -- obra, byn, simple faz, 1/4 de a4, común, 100
(1, 1, 2, 3, 1, 2, 7600), -- obra, byn, doble faz, 1/4 de a4, común, 100
(1, 1, 1, 3, 1, 4, 9750), -- obra, byn, simple faz, 1/4 de a4, común, 200
(1, 1, 2, 3, 1, 4, 13500), -- obra, byn, doble faz, 1/4 de a4, común, 200
(1, 1, 1, 3, 1, 7, 21900), -- obra, byn, simple faz, 1/4 de a4, común, 500
(1, 1, 2, 3, 1, 7, 27300), -- obra, byn, doble faz, 1/4 de a4, común, 500
(1, 1, 1, 3, 1, 8, 41100), -- obra, byn, simple faz, 1/4 de a4, común, 1000
(1, 1, 2, 3, 1, 8, 47500), -- obra, byn, doble faz, 1/4 de a4, común, 1000
(1, 2, 1, 3, 1, 2, 14850), -- obra, color, simple faz, 1/4 de a4, común, 100
(1, 2, 2, 3, 1, 2, 16750), -- obra, color, doble faz, 1/4 de a4, común, 100
(1, 2, 1, 3, 1, 4, 26700), -- obra, color, simple faz, 1/4 de a4, común, 200
(1, 2, 2, 3, 1, 4, 29800), -- obra, color, doble faz, 1/4 de a4, común, 200
(1, 2, 1, 3, 1, 7, 64000), -- obra, color, simple faz, 1/4 de a4, común, 500
(1, 2, 2, 3, 1, 7, 77500), -- obra, color, doble faz, 1/4 de a4, común, 500
(1, 2, 1, 3, 1, 8, 111400), -- obra, color, simple faz, 1/4 de a4, común, 1000
(1, 2, 2, 3, 1, 8, 139600), -- obra, color, doble faz, 1/4 de a4, común, 1000
(2, 2, 1, 3, 1, 2, 18150), -- 115, color, simple faz, 1/4 de a4, común, 100
(2, 2, 2, 3, 1, 2, 21600), -- 115, color, doble faz, 1/4 de a4, común, 100
(2, 2, 1, 3, 1, 4, 31950), -- 115, color, simple faz, 1/4 de a4, común, 200
(2, 2, 2, 3, 1, 4, 36900), -- 115, color, doble faz, 1/4 de a4, común, 200
(2, 2, 1, 3, 1, 7, 77550), -- 115, color, simple faz, 1/4 de a4, común, 500
(2, 2, 2, 3, 1, 7, 87100), -- 115, color, doble faz, 1/4 de a4, común, 500
(2, 2, 1, 3, 1, 8, 140450), -- 115, color, simple faz, 1/4 de a4, común, 1000
(2, 2, 2, 3, 1, 8, 159100); -- 115, color, doble faz, 1/4 de a4, común, 1000

-- hojas membreteadas
create table if not exists medida_hojas_membreteadas (
    id bigint auto_increment not null primary key,
    medida varchar(255) not null
);

insert into medida_hojas_membreteadas(medida) values
('A4'),
('OTRA');

create table if not exists tipo_color_hojas_membreteadas (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_color_hojas_membreteadas(tipo) values
('BLANCO Y NEGRO'),
('A COLOR');

create table if not exists cantidad_hojas_membreteadas (
    id bigint auto_increment not null primary key,
    cantidad varchar(255) not null,
);

insert into cantidad_hojas_membreteadas(cantidad) values
('1'),
('2'),
('4'),
('OTRA');

create table if not exists hojas_membreteadas (
    id bigint auto_increment not null primary key,
    medida_personalizada varchar(255) null,
    cantidad_personalizada varchar(255) null,
    cantidad_hojas int not null,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 5000,
    precio int null,
    informacion_adicional varchar(255) null,
    id_medida_hojas_membreteadas bigint not null,
    id_tipo_color_hojas_membreteadas bigint not null,
    id_cantidad_hojas_membreteadas bigint not null,
    constraint fk_medida_hojas_membreteadas foreign key(id_medida_hojas_membreteadas) references medida_hojas_membreteadas(id),
    constraint fk_tipo_color_hojas_membreteadas foreign key(id_tipo_color_hojas_membreteadas) references tipo_color_hojas_membreteadas(id),
    constraint fk_cantidad_hojas_membreteadas foreign key(id_cantidad_hojas_membreteadas) references cantidad_hojas_membreteadas(id)
);

create table if not exists plantilla_hojas_membreteadas (
    id bigint auto_increment not null primary key,
    precio int not null,
    cantidad_hojas int not null,
    id_medida_hojas_membreteadas bigint not null,
    id_tipo_color_hojas_membreteadas bigint not null,
    id_cantidad_hojas_membreteadas bigint not null,
    constraint fk_plantilla_medida_hojas_membreteadas foreign key(id_medida_hojas_membreteadas) references medida_hojas_membreteadas(id),
    constraint fk_plantilla_tipo_color_hojas_membreteadas foreign key(id_tipo_color_hojas_membreteadas) references tipo_color_hojas_membreteadas(id),
    constraint fk_plantilla_cantidad_hojas_membreteadas foreign key(id_cantidad_hojas_membreteadas) references cantidad_hojas_membreteadas(id)
);

insert into plantilla_hojas_membreteadas(cantidad_hojas, id_medida_hojas_membreteadas, id_tipo_color_hojas_membreteadas, id_cantidad_hojas_membreteadas, precio) values
(100, 1, 1, 1, 11900),  -- 100, A4, byn, 1 u.
(100, 1, 1, 2, 21550),  -- 100, A4, byn, 2 u.
(100, 1, 1, 3, 41000),  -- 100, A4, byn, 4 u.
(100, 1, 2, 1, 36150),  -- 100, A4, color, 1 u.
(100, 1, 2, 2, 64300),  -- 100, A4, color, 2 u.
(100, 1, 2, 3, 119900); -- 100, A4, color, 4 u.

-- impresiones
create table if not exists tipo_color_impresion (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_color_impresion(tipo) values
('BLANCO Y NEGRO'),
('A COLOR');

create table if not exists tamanio_hoja_impresion (
    id bigint auto_increment not null primary key,
    tamanio varchar(255) not null
);

insert into tamanio_hoja_impresion(tamanio) values
('A4'),
('A5'),
('OFICIO'),
('A3'),
('A3+'),
('OTRO');

create table if not exists tipo_faz_impresion (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_faz_impresion(tipo) values
('SIMPLE FAZ'),
('DOBLE FAZ');

create table if not exists tipo_papel_impresion (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_papel_impresion(tipo) values
('COPIA COMÚN'),
('OBRA 75 GRS'),
('ILUSTRACIÓN 90 GRS'),
('ILUSTRACIÓN 115 GRS'),
('ILUSTRACIÓN 150 GRS'),
('ILUSTRACIÓN 200 GRS'),
('ILUSTRACIÓN 220 GRS'),
('ILUSTRACIÓN 250 GRS'),
('ILUSTRACIÓN 300 GRS'),
('OPALINA 150 GRS'),
('OPALINA 180 GRS'),
('OPALINA 210 GRS'),
('KRAFT 200 GRS'),
('OPALINA 200 GRS');

create table if not exists cantidad_impresion (
    id bigint auto_increment not null primary key,
    cantidad varchar(255) not null
);

insert into cantidad_impresion(cantidad) values
('1 - 10'),
('11 - 100'),
('+100'),
('INDISTINTO');

create table if not exists tipo_impresion (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_impresion(tipo) values
('PARTICULAR'),
('ESCOLAR/UNIVERSITARIO');

create table if not exists impresion (
    id bigint auto_increment not null primary key,
    es_anillado tinyint(1) not null default 0,
    cantidad_detalle int null,
    enlace_archivo varchar(255) not null default '-',
    precio int null,
    informacion_adicional varchar(255) null,
    id_tipo_color_impresion bigint not null,
    id_tamanio_hoja_impresion bigint not null,
    id_tipo_faz_impresion bigint not null,
    id_tipo_papel_impresion bigint not null,
    id_cantidad_impresion bigint not null,
    id_tipo_impresion bigint not null,
    constraint fk_tipo_color_impresion foreign key(id_tipo_color_impresion) references tipo_color_impresion(id),
    constraint fk_tamanio_hoja_impresion foreign key(id_tamanio_hoja_impresion) references tamanio_hoja_impresion(id),
    constraint fk_tipo_faz_impresion foreign key(id_tipo_faz_impresion) references tipo_faz_impresion(id),
    constraint fk_tipo_papel_impresion foreign key(id_tipo_papel_impresion) references tipo_papel_impresion(id),
    constraint fk_cantidad_impresion foreign key(id_cantidad_impresion) references cantidad_impresion(id),
    constraint fk_tipo_impresion foreign key(id_tipo_impresion) references tipo_impresion(id)
);

create table if not exists plantilla_impresion (
    id bigint auto_increment not null primary key,
    precio int not null, -- por página (si la hoja doble faz está a 150, puse 75)
    id_tipo_color_impresion bigint not null,
    id_tamanio_hoja_impresion bigint not null,
    id_tipo_faz_impresion bigint not null,
    id_tipo_papel_impresion bigint not null,
    id_cantidad_impresion bigint not null,
    id_tipo_impresion bigint not null,
    constraint fk_plantilla_tipo_color_impresion foreign key(id_tipo_color_impresion) references tipo_color_impresion(id),
    constraint fk_plantilla_tamanio_hoja_impresion foreign key(id_tamanio_hoja_impresion) references tamanio_hoja_impresion(id),
    constraint fk_plantilla_tipo_faz_impresion foreign key(id_tipo_faz_impresion) references tipo_faz_impresion(id),
    constraint fk_plantilla_tipo_papel_impresion foreign key(id_tipo_papel_impresion) references tipo_papel_impresion(id),
    constraint fk_plantilla_cantidad_impresion foreign key(id_cantidad_impresion) references cantidad_impresion(id),
    constraint fk_plantilla_tipo_impresion foreign key(id_tipo_impresion) references tipo_impresion(id)
);

insert into plantilla_impresion(id_tipo_color_impresion, id_tamanio_hoja_impresion, id_tipo_faz_impresion, id_tipo_papel_impresion, id_cantidad_impresion, id_tipo_impresion, precio) values
(1, 1, 1, 1, 1, 1, 100),   -- byn, a4, simple, copia, 1-10, particular
(1, 1, 1, 1, 2, 1, 80),    -- byn, a4, simple, copia, 11-100, particular
(1, 1, 1, 1, 3, 1, 60),    -- byn, a4, simple, copia, +100, particular
(1, 1, 2, 1, 1, 1, 75),    -- byn, a4, doble, copia, 1-10, particular
(1, 1, 2, 1, 2, 1, 60),    -- byn, a4, doble, copia, 11-100, particular
(1, 1, 2, 1, 3, 1, 50),    -- byn, a4, doble, copia, +100, particular
(1, 3, 1, 1, 1, 1, 150),   -- byn, oficio, simple, copia, 1-10, particular
(1, 3, 1, 1, 2, 1, 130),   -- byn, oficio, simple, copia, 11-100, particular
(1, 3, 1, 1, 3, 1, 100),   -- byn, oficio, simple, copia, +100, particular
(1, 3, 2, 1, 1, 1, 85),    -- byn, oficio, doble, copia, 1-10, particular
(1, 3, 2, 1, 2, 1, 75),    -- byn, oficio, doble, copia, 11-100, particular
(1, 3, 2, 1, 3, 1, 60),    -- byn, oficio, doble, copia, +100, particular
(2, 1, 1, 2, 4, 1, 500),   -- color, a4, simple, obra, indistinto, particular
(2, 1, 2, 2, 4, 1, 800),   -- color, a4, doble, obra, indistinto, particular
(2, 1, 1, 3, 4, 1, 900),   -- color, a4, simple, ilu. 90, indistinto, particular
(2, 1, 2, 3, 4, 1, 900),   -- color, a4, doble, ilu. 90, indistinto, particular
(2, 1, 1, 4, 4, 1, 900),   -- color, a4, simple, ilu. 115, indistinto particular
(2, 1, 2, 4, 4, 1, 900),   -- color, a4, doble, ilu. 115, indistinto, particular
(2, 1, 1, 5, 4, 1, 1100),  -- color, a4, simple, ilu. 150, indistinto, particular
(2, 1, 2, 5, 4, 1, 1100),  -- color, a4, doble, ilu. 150, indistinto, particular
(2, 1, 1, 6, 4, 1, 1350),  -- color, a4, simple, ilu. 200, indistinto, particular
(2, 1, 2, 6, 4, 1, 1350),  -- color, a4, doble, ilu. 200, indistinto, particular
(2, 1, 1, 7, 4, 1, 1350),  -- color, a4, simple, ilu. 220, indistinto, particular
(2, 1, 2, 7, 4, 1, 1350),  -- color, a4, doble, ilu. 220, indistinto, particular
(2, 1, 1, 8, 4, 1, 1500),  -- color, a4, simple, ilu. 250, indistinto, particular
(2, 1, 2, 8, 4, 1, 1500),  -- color, a4, doble, ilu. 250, indistinto, particular
(2, 1, 1, 9, 4, 1, 1700),  -- color, a4, simple, ilu. 300, indistinto, particular
(2, 1, 2, 9, 4, 1, 1700),  -- color, a4, doble, ilu. 300, indistinto, particular
(2, 1, 1, 10, 4, 1, 950),  -- color, a4, simple, opa. 150, indistinto, particular
(2, 1, 2, 10, 4, 1, 950),  -- color, a4, doble, opa. 150, indistinto, particular
(2, 1, 1, 11, 4, 1, 1200), -- color, a4, simple, opa. 180, indistinto, particular
(2, 1, 2, 11, 4, 1, 1200), -- color, a4, doble, opa. 180, indistinto, particular
(2, 1, 1, 12, 4, 1, 1350), -- color, a4, simple, opa. 210, indistinto, particular
(2, 1, 2, 12, 4, 1, 1350), -- color, a4, doble, opa. 210, indistinto, particular
(1, 1, 1, 13, 4, 1, 1400), -- byn, a4, simple, kraft, indistinto, particular
(1, 1, 2, 13, 4, 1, 1400), -- byn, a4, doble, kraft, indistinto, particular
(2, 4, 1, 2, 4, 1, 900),   -- color, a3, simple, obra, indistinto, particular
(2, 4, 2, 2, 4, 1, 1100),  -- color, a3, doble, obra, indistinto, particular
(2, 4, 1, 5, 4, 1, 2100),  -- color, a3, simple, ilu. 150, indistinto, particular
(2, 4, 2, 5, 4, 1, 2100),  -- color, a3, doble, ilu. 150, indistinto, particular
(2, 4, 1, 6, 4, 1, 2300),  -- color, a3, simple, ilu. 200, indistinto, particular
(2, 4, 2, 6, 4, 1, 2300),  -- color, a3, doble, ilu. 200, indistinto, particular
(2, 4, 1, 8, 4, 1, 2500),  -- color, a3, simple, ilu. 250, indistinto, particular
(2, 4, 2, 8, 4, 1, 2500),  -- color, a3, doble, ilu. 250, indistinto, particular
(2, 4, 1, 9, 4, 1, 2800),  -- color, a3, simple, ilu. 300, indistinto, particular
(2, 4, 2, 9, 4, 1, 2800),  -- color, a3, doble, ilu. 300, indistinto, particular
(2, 4, 1, 10, 4, 1, 1900), -- color, a3, simple, opa. 150, indistinto, particular
(2, 4, 2, 10, 4, 1, 1900), -- color, a3, doble, opa. 150, indistinto, particular
(2, 4, 1, 11, 4, 1, 2100), -- color, a3, simple, opa. 180, indistinto, particular
(2, 4, 2, 11, 4, 1, 2100), -- color, a3, doble, opa. 180, indistinto, particular
(2, 4, 1, 14, 4, 1, 2300), -- color, a3, simple, opa. 200, indistinto, particular
(2, 4, 2, 14, 4, 1, 2300), -- color, a3, doble, opa. 200, indistinto, particular
(1, 4, 1, 13, 4, 1, 2300), -- byn, a3, simple, kraft, indistinto, particular
(1, 4, 2, 13, 4, 1, 2300), -- byn, a3, doble, kraft, indistinto, particular
(2, 5, 1, 5, 4, 1, 2600),  -- color, a3+, simple, ilu. 150, indistinto, particular
(2, 5, 2, 5, 4, 1, 2600),  -- color, a3+, doble, ilu. 150, indistinto, particular
(2, 5, 1, 6, 4, 1, 2800),  -- color, a3+, simple, ilu. 200, indistinto, particular
(2, 5, 2, 6, 4, 1, 2800),  -- color, a3+, doble, ilu. 200, indistinto, particular
(2, 5, 1, 8, 4, 1, 3000),  -- color, a3+, simple, ilu. 250, indistinto, particular
(2, 5, 2, 8, 4, 1, 3000),  -- color, a3+, doble, ilu. 250, indistinto, particular
(1, 1, 1, 1, 4, 2, 60), -- byn, a4, simple, copia, indistinto, escolar
(1, 1, 2, 1, 4, 2, 40), -- byn, a4, doble, copia, indistinto, escolar
(1, 3, 1, 1, 4, 2, 80), -- byn, oficio, simple, copia, indistinto, escolar
(1, 3, 2, 1, 4, 2, 50), -- byn, oficio, doble, copia, indistinto, escolar
(2, 1, 1, 1, 4, 2, 400), -- color, a4, simple, copia, indistinto, escolar
(2, 1, 2, 1, 4, 2, 325), -- color, a4, doble, copia, indistinto, escolar
(2, 3, 1, 1, 4, 2, 500), -- color, oficio, simple, copia, indistinto, escolar
(2, 3, 2, 1, 4, 2, 375); -- color, oficio, doble, copia, indistinto, escolar

-- lonas
create table if not exists medida_lona_comun (
    id bigint auto_increment not null primary key,
    medida varchar(255) not null
);

insert into medida_lona_comun(medida) values
('40X60 CM'),
('60X90 CM'),
('70X100 CM'),
('90X120 CM'),
('OTRA');

create table if not exists tipo_lona_comun (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_lona_comun(tipo) values
('LONA FRONT'),
('LONA BACK LIGHT'),
('LONA BLOCKOUT');

create table if not exists lona_comun (
    id bigint auto_increment not null primary key,
    medida_personalizada varchar(255) null,
    con_ojales tinyint(1) not null default 0,
    con_ojales_con_refuerzo tinyint(1) not null default 0,
    con_bolsillos tinyint(1) not null default 0,
    con_demasia_para_tensado tinyint(1) not null default 0,
    con_solapado tinyint(1) not null default 0,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 5000,
    precio int null,
    informacion_adicional varchar(255) null,
    id_medida_lona_comun bigint not null,
    id_tipo_lona_comun bigint not null,
    constraint fk_medida_lona_comun foreign key(id_medida_lona_comun) references medida_lona_comun(id),
    constraint fk_tipo_lona_comun foreign key(id_tipo_lona_comun) references tipo_lona_comun(id)
);

create table if not exists plantilla_lona_comun (
    id bigint auto_increment not null primary key,
    precio int not null,
    con_bolsillos tinyint(1) not null,
    id_medida_lona_comun bigint not null,
    id_tipo_lona_comun bigint not null,
    constraint fk_plantilla_medida_lona_comun foreign key(id_medida_lona_comun) references medida_lona_comun(id),
    constraint fk_plantilla_tipo_lona_comun foreign key(id_tipo_lona_comun) references tipo_lona_comun(id)
);

insert into plantilla_lona_comun(con_bolsillos, id_medida_lona_comun, id_tipo_lona_comun, precio) values
(1, 1, 1, 20440), -- con bolsillos, 40x60, front
(1, 2, 1, 23600), -- con bolsillos, 60x90, front
(1, 3, 1, 27650), -- con bolsillos, 70x100, front
(1, 4, 1, 29930); -- con bolsillos, 90x120, front

-- lonas publicitarias
create table if not exists tipo_lona_publicitaria (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_lona_publicitaria(tipo) values
('LONA FRONT'),
('LONA BACK LIGHT'),
('LONA BLOCKOUT');

create table if not exists medida_lona_publicitaria (
    id bigint auto_increment not null primary key,
    medida varchar(255) not null
);

insert into medida_lona_publicitaria(medida) values
('60X160 CM'),
('90X190 CM'),
('85X200 CM'),
('A3 MINI - 25X42 CM');

create table if not exists lona_publicitaria (
    id bigint auto_increment not null primary key,
    con_adicional_portabanner tinyint(1) not null default 0,
    con_ojales tinyint(1) not null default 0,
    con_ojales_con_refuerzo tinyint(1) not null default 0,
    con_bolsillos tinyint(1) not null default 0,
    con_demasia_para_tensado tinyint(1) not null default 0,
    con_solapado tinyint(1) not null default 0,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 8000,
    precio int null,
    informacion_adicional varchar(255) null,
    id_medida_lona_publicitaria bigint not null,
    id_tipo_lona_publicitaria bigint not null,
    constraint fk_medida_lona_publicitaria foreign key(id_medida_lona_publicitaria) references medida_lona_publicitaria(id),
    constraint fk_tipo_lona_publicitaria foreign key(id_tipo_lona_publicitaria) references tipo_lona_publicitaria(id)
);

create table if not exists plantilla_lona_publicitaria (
    id bigint auto_increment not null primary key,
    precio int not null,
    con_adicional_portabanner tinyint(1) not null,
    con_ojales tinyint(1) not null default 0,
    con_ojales_con_refuerzo tinyint(1) not null default 0,
    con_bolsillos tinyint(1) not null default 0,
    con_demasia_para_tensado tinyint(1) not null default 0,
    con_solapado tinyint(1) not null default 0,
    id_medida_lona_publicitaria bigint not null,
    id_tipo_lona_publicitaria bigint not null,
    constraint fk_plantilla_medida_lona_publicitaria foreign key(id_medida_lona_publicitaria) references medida_lona_publicitaria(id),
    constraint fk_plantilla_tipo_lona_publicitaria foreign key(id_tipo_lona_publicitaria) references tipo_lona_publicitaria(id)
);

insert into plantilla_lona_publicitaria(con_adicional_portabanner, con_ojales, con_ojales_con_refuerzo, con_bolsillos, con_demasia_para_tensado, con_solapado, id_medida_lona_publicitaria, id_tipo_lona_publicitaria, precio) values
(1, 0, 0, 0, 0, 0, 4, 1, 19500), -- con portabanner, sin ojales, sin bolsillos, sin demasia, sin solapado, 25x42, front
(1, 0, 0, 0, 0, 0, 1, 1, 56016), -- con portabanner sin ojales, sin bolsillos, sin demasia, sin solapado, 60x160, front
(1, 0, 0, 0, 0, 0, 2, 1, 81480), -- con portabanner, sin ojales, sin bolsillos, sin demasia, sin solapado, 90x190, front
(1, 0, 0, 0, 0, 0, 3, 1, 85510), -- con portabanner, sin ojales, sin bolsillos, sin demasia, sin solapado, 85x200, front
(0, 0, 1, 0, 0, 0, 4, 1, 15900), -- sin portabanner, con ojales met., sin bolsillos, sin demasia, sin solapado, 25x42, front
(0, 0, 1, 0, 0, 0, 1, 1, 29800), -- sin portabanner, con ojales met., sin bolsillos, sin demasia, sin solapado, 60x160, front
(0, 0, 0, 1, 0, 0, 2, 1, 39800), -- sin portabanner, sin ojales, con bolsillos, sin demasia, sin solapado, 90x190, front
(0, 0, 0, 0, 1, 0, 3, 1, 42200); -- sin portabanner, sin ojales, sin bolsillos, con demasia, sin solapado, 85x200, front

-- rifas / bonos contribución
create table if not exists tipo_papel_rifa (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_papel_rifa(tipo) values
('OBRA 75 GRS'),
('ILUSTRACIÓN 150 GRS');

create table if not exists tipo_troquelado_rifa (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_troquelado_rifa(tipo) values
('SIN TROQUELAR'),
('TROQUELADO SIMPLE'),
('TROQUELADO DOBLE');

create table if not exists tipo_color_rifa (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_color_rifa(tipo) values
('BLANCO Y NEGRO'),
('A COLOR');

create table if not exists rifas_bonos_contribucion (
    id bigint auto_increment not null primary key,
    con_numerado tinyint(1) not null default 0,
    detalle_numerado varchar(255) null,
    con_encolado tinyint(1) not null default 0,
    medida varchar(255) not null,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 5000,
    precio int null,
    informacion_adicional varchar(255) null,
    id_tipo_papel_rifa bigint not null,
    id_tipo_troquelado_rifa bigint not null,
    id_tipo_color_rifa bigint not null,
    constraint fk_tipo_papel_rifa foreign key(id_tipo_papel_rifa) references tipo_papel_rifa(id),
    constraint fk_tipo_troquelado_rifa foreign key(id_tipo_troquelado_rifa) references tipo_troquelado_rifa(id),
    constraint fk_tipo_color_rifa foreign key(id_tipo_color_rifa) references tipo_color_rifa(id)
);

-- rotulaciones
create table if not exists tipo_rotulacion (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_rotulacion(tipo) values
('ARLON BRILLANTE'),
('ARLON MATE'),
('CLEAR'),
('MICROPERFORADO'),
('VINILO DE CORTE');

create table if not exists tipo_corte_rotulacion (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_corte_rotulacion(tipo) values
('MANUAL'),
('TROQUELADO'),
('TROQUELADO CON CORTE INDIVIDUAL'),
('PLANCHA IMPRESA');

create table if not exists rotulacion (
    id bigint auto_increment not null primary key,
    es_laminado tinyint(1) not null default 0,
    horario_rotulacion varchar(255) not null default '-',
    direccion_rotulacion varchar(255) not null default '-',
    medida varchar(255) not null,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 5000,
    precio int null,
    informacion_adicional varchar(255) null,
    id_tipo_rotulacion bigint not null,
    id_tipo_corte_rotulacion bigint not null,
    constraint fk_tipo_rotulacion foreign key(id_tipo_rotulacion) references tipo_rotulacion(id),
    constraint fk_tipo_corte_rotulacion foreign key(id_tipo_corte_rotulacion) references tipo_corte_rotulacion(id)
);

-- sellos automáticos
create table if not exists modelo_sello_automatico (
    id bigint auto_increment not null primary key,
    modelo varchar(255) not null
);

insert into modelo_sello_automatico(modelo) values
('MOUSE STAMP 20 COLOP - 14X38 MM'),
('POCKET 20 COLOP - 14X38 MM'),
('AUT 20 COLOP - 14X38 MM'),
('AUT 10 COLOP - 10X27 MM'),
('PRINTER C30 - 18X47 MM'),
('PRINTER 45 - 82X25 MM'),
('PRINTER C50 - 30X69 MM'),
('PRINTER 55 - 40X60 MM'),
('PRINTER 55 DATER - 40X60 MM'),
('PRINTER C60 - 76X37 MM'),
('S260 - 45X24 MM'),
('MINI DATER S120 - 40X45 MM'),
('MINI DATER S160 - 40X20 MM'),
('PRINTER R30'),
('PRINTER R40'),
('PRINTER R24'),
('PRINTER R17'),
('PRINTER 52 DATER'),
('PRINTER S120 MINI DATER'),
('PRINTER 53'),
('PRINTER Q24 DATER');

create table if not exists sello_automatico (
    id bigint auto_increment not null primary key,
    es_profesional tinyint(1) not null default 0,
    es_particular tinyint(1) not null default 0,
    texto_linea_uno varchar(255) not null,
    texto_linea_dos varchar(255) not null default '-',
    texto_linea_tres varchar(255) not null default '-',
    texto_linea_cuatro varchar(255) not null default '-',
    tipografia_linea_uno varchar(255) not null,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 5000,
    precio int null,
    informacion_adicional varchar(255) null,
    id_modelo_sello_automatico bigint not null,
    constraint fk_modelo_sello_automatico foreign key(id_modelo_sello_automatico) references modelo_sello_automatico(id)
);

create table if not exists plantilla_sello_automatico (
    id bigint auto_increment not null primary key,
    precio int not null,
    id_modelo_sello_automatico bigint not null,
    constraint fk_plantilla_modelo_sello_automatico foreign key(id_modelo_sello_automatico) references modelo_sello_automatico(id)
);

insert into plantilla_sello_automatico(id_modelo_sello_automatico, precio) values
(1, 22850),   -- mouse stamp 20
(2, 22250),   -- pocket 20
(3, 18850),   -- aut 20
(4, 15800),   -- aut 10
(5, 30000),   -- printer c30
(6, 44400),   -- printer 45
(7, 42900),   -- printer c50
(8, 44800),   -- printer 55
(9, 51900),   -- printer 55 dater
(10, 46500),  -- printer c60
(11, 48200), -- printer s 260
(12, 43050),  -- mini dater s120
(13, 27350),  -- mini dater s160
(14, 40500),  -- printer r30
(15, 43450),  -- printer r40
(16, 25900),  -- printer r24
(17, 18500),  -- printer r17
(18, 49900),  -- printer 52 dater
(19, 17900),  -- s120 mini dater
(20, 31900),  -- printer 53
(21, 49700);  -- printer q24 dater

-- sellos automáticos escolares
create table if not exists modelo_sello_automatico_escolar (
    id bigint auto_increment not null primary key,
    modelo varchar(255) not null
);

insert into modelo_sello_automatico_escolar(modelo) values
('10X27 MM - 10 COLOP'),
('14X38 MM - 20 COLOP');

create table if not exists sello_automatico_escolar (
    id bigint auto_increment not null primary key,
    texto_linea_uno varchar(255) not null,
    texto_linea_dos varchar(255) not null default '-',
    texto_linea_tres varchar(255) not null default '-',
    tipografia varchar(255) not null,
    dibujo varchar(255) not null,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 5000,
    precio int null,
    informacion_adicional varchar(255) null,
    id_modelo_sello_automatico_escolar bigint not null,
    constraint fk_modelo_sello_automatico_escolar foreign key(id_modelo_sello_automatico_escolar) references modelo_sello_automatico_escolar(id)
);

create table if not exists plantilla_sello_automatico_escolar (
    id bigint auto_increment not null primary key,
    precio int not null,
    id_modelo_sello_automatico_escolar bigint not null,
    constraint fk_plantilla_modelo_sello_automatico_escolar foreign key(id_modelo_sello_automatico_escolar) references modelo_sello_automatico_escolar(id)
);

insert into plantilla_sello_automatico_escolar(id_modelo_sello_automatico_escolar, precio) values
(1, 15800),
(2, 18850);

-- sellos de madera
create table if not exists tamanio_sello_madera (
    id bigint auto_increment not null primary key,
    tamanio varchar(255) not null
);

insert into tamanio_sello_madera(tamanio) values
('6X3 CM'),
('6X4 CM'),
('6X5 CM'),
('6X7 CM'),
('6X8 CM'),
('7X3 CM'),
('7X5 CM'),
('1X1 CM'),
('2X2 CM'),
('3X3 CM'),
('4X4 CM'),
('5X5 CM'),
('6X6 CM'),
('7X7 CM'),
('8X8 CM'),
('9X9 CM'),
('10X10 CM'),
('11X11 CM'),
('12X12 CM'),
('13X13 CM'),
('14X14 CM'),
('15X15 CM'),
('10X15 CM'),
('OTRO');

create table if not exists sello_madera (
    id bigint auto_increment not null primary key,
    tamanio_personalizado varchar(255) null,
    con_adicional_perilla tinyint(1) not null default 0,
    precio_adicional_perilla int not null default 2800,
    detalle_sello varchar(255) not null default '-',
    tipografia_linea_uno varchar(255) not null,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 6000,
    precio int null,
    informacion_adicional varchar(255) null,
    id_tamanio_sello_madera bigint not null,
    constraint fk_tamanio_sello_madera foreign key(id_tamanio_sello_madera) references tamanio_sello_madera(id)
);

create table if not exists plantilla_sello_madera (
    id bigint auto_increment not null primary key,
    precio int not null,
    id_tamanio_sello_madera bigint not null,
    constraint fk_plantilla_tamanio_sello_madera foreign key(id_tamanio_sello_madera) references tamanio_sello_madera(id)
);

insert into plantilla_sello_madera(id_tamanio_sello_madera, precio) values
(1, 16200),  -- 6x3
(2, 17850),  -- 6x4
(3, 19750),  -- 6x5
(4, 20650),  -- 6x7
(5, 21750),  -- 6x8
(6, 17850),  -- 7x3
(7, 21750),  -- 7x5
(8, 8100),   -- 1x1
(9, 8800),   -- 2x2
(10, 10550), -- 3x3
(11, 11650), -- 4x4
(12, 12450), -- 5x5
(13, 16200), -- 6x6
(14, 19350), -- 7x7
(15, 22800), -- 8x8
(16, 28000), -- 9x9
(17, 30350), -- 10x10
(18, 32650), -- 11x11
(19, 38850), -- 12x12
(20, 44400), -- 13x13
(21, 52500), -- 14x14
(22, 56600), -- 15x15
(23, 47800); -- 10x15

-- sobres
create table if not exists tipo_color_sobre (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_color_sobre(tipo) values
('BLANCO Y NEGRO'),
('A COLOR');

create table if not exists medida_sobre (
    id bigint auto_increment not null primary key,
    medida varchar(255) not null
);

insert into medida_sobre(medida) values
('BOLSA A4 - 27X37 CM'),
('BOLSA A5 - 19X24 CM'),
('OFICIO INGLÉS - 23,5X12 CM'),
('OTRA');

create table if not exists cantidad_sobre (
    id bigint auto_increment not null primary key,
    cantidad varchar(255) not null
);

insert into cantidad_sobre(cantidad) values
('100'),
('200'),
('400'),
('OTRA');

create table if not exists sobre (
    id bigint auto_increment not null primary key,
    medida_personalizada varchar(255) null,
    cantidad_personalizada varchar(255) null,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 5000,
    precio int null,
    informacion_adicional varchar(255) null,
    id_medida_sobre bigint not null,
    id_tipo_color_sobre bigint not null,
    id_cantidad_sobre bigint not null,
    constraint fk_medida_sobre foreign key(id_medida_sobre) references medida_sobre(id),
    constraint fk_tipo_color_sobre foreign key(id_tipo_color_sobre) references tipo_color_sobre(id),
    constraint fk_cantidad_sobre foreign key(id_cantidad_sobre) references cantidad_sobre(id)
);

create table if not exists plantilla_sobre (
    id bigint auto_increment not null primary key,
    precio int not null,
    id_medida_sobre bigint not null,
    id_tipo_color_sobre bigint not null,
    id_cantidad_sobre bigint not null,
    constraint fk_plantilla_medida_sobre foreign key(id_medida_sobre) references medida_sobre(id),
    constraint fk_plantilla_tipo_color_sobre foreign key(id_tipo_color_sobre) references tipo_color_sobre(id),
    constraint fk_plantilla_cantidad_sobre foreign key(id_cantidad_sobre) references cantidad_sobre(id)
);

insert into plantilla_sobre(id_medida_sobre, id_tipo_color_sobre, id_cantidad_sobre, precio) values
(3, 1, 1, 55350),  -- oficio inglés, byn, 100
(3, 1, 2, 99750),  -- oficio inglés, byn, 200
(3, 1, 3, 182800), -- oficio inglés, byn, 400
(1, 1, 1, 65950),  -- bolsa A4, byn, 100
(1, 1, 2, 107050), -- bolsa A4, byn, 200
(1, 1, 3, 195600), -- bolsa A4, byn, 400
(1, 2, 1, 122800),  -- bolsa A4, color, 100
(1, 2, 2, 182000), -- bolsa A4, color, 200
(1, 2, 3, 385700), -- bolsa A4, color, 400
(2, 1, 1, 33000),  -- bolsa A5, byn, 100
(2, 1, 2, 53600), -- bolsa A5, byn, 200
(2, 1, 3, 97800), -- bolsa A5, byn, 400
(2, 2, 1, 61500),  -- bolsa A5, color, 100
(2, 2, 2, 91150), -- bolsa A5, color, 200
(2, 2, 3, 192950); -- bolsa A5, color, 400

-- stickers
create table if not exists tipo_troquelado_sticker (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_troquelado_sticker(tipo) values
('CUADRADO'),
('CIRCULAR'),
('POR EL CONTORNO'),
('CORTE INDIVIDUAL'),
('SIN TROQUELAR');

create table if not exists medida_sticker (
    id bigint auto_increment not null primary key,
    medida varchar(255) not null
);

insert into medida_sticker(medida) values
('5 CM'),
('7 CM'),
('10 CM'),
('OTRA');

create table if not exists cantidad_sticker (
    id bigint auto_increment not null primary key,
    cantidad varchar(255) not null
);

insert into cantidad_sticker(cantidad) values
('100'),
('200'),
('500'),
('OTRA');

create table if not exists sticker (
    id bigint auto_increment not null primary key,
    medida_personalizada varchar(255) null,
    cantidad_personalizada varchar(255) null,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 2000,
    precio int null,
    informacion_adicional varchar(255) null,
    id_tipo_troquelado_sticker bigint not null,
    id_cantidad_sticker bigint not null,
    id_medida_sticker bigint not null,
    constraint fk_tipo_troquelado_sticker foreign key(id_tipo_troquelado_sticker) references tipo_troquelado_sticker(id),
    constraint fk_cantidad_sticker foreign key(id_cantidad_sticker) references cantidad_sticker(id),
    constraint fk_medida_sticker foreign key(id_medida_sticker) references medida_sticker(id)
);

create table if not exists plantilla_sticker (
    id bigint auto_increment not null primary key,
    precio int not null,
    id_tipo_troquelado_sticker bigint not null,
    id_cantidad_sticker bigint not null,
    id_medida_sticker bigint not null,
    constraint fk_plantilla_tipo_troquelado_sticker foreign key(id_tipo_troquelado_sticker) references tipo_troquelado_sticker(id),
    constraint fk_plantilla_cantidad_sticker foreign key(id_cantidad_sticker) references cantidad_sticker(id),
    constraint fk_plantilla_medida_sticker foreign key(id_medida_sticker) references medida_sticker(id)
);

insert into plantilla_sticker(id_tipo_troquelado_sticker, id_cantidad_sticker, id_medida_sticker, precio) values
(3, 1, 1, 9850), -- por el contorno, 100, 5 cm
(3, 2, 1, 18600), -- por el contorno, 200, 5 cm
(3, 3, 1, 43600), -- por el contorno, 500, 5 cm
(3, 1, 2, 18000), -- por el contorno, 100, 7 cm
(3, 2, 2, 30850), -- por el contorno, 200, 7 cm
(3, 3, 2, 75150), -- por el contorno, 500, 7 cm
(3, 1, 3, 23700), -- por el contorno, 100, 10 cm
(3, 2, 3, 42300), -- por el contorno, 200, 10 cm
(3, 3, 3, 94650); -- por el contorno, 500, 10 cm

-- sublimaciones
create table if not exists material_sublimacion (
    id bigint auto_increment not null primary key,
    material varchar(255) not null
);

insert into material_sublimacion(material) values
('BIROME'),
('TAZA DE POLÍMERO'),
('TAZA DE CERÁMICA'),
('TAZA DE CERÁMICA CON DETALLE'),
('TAZA MÁGICA'),
('TELA'),
('MATE DE POLÍMERO'),
('MATE DE CERÁMICA'),
('JARRO DE CAFÉ DE POLÍMERO'),
('LAPICERO DE POLÍMERO'),
('CINTA FALLETINA'),
('LLAVERO CORTO LANYARD'),
('LLAVERO LARGO LANYARD'),
('MOUSEPAD');

create table if not exists cantidad_sublimacion (
    id bigint auto_increment not null primary key,
    cantidad varchar(255) not null
);

insert into cantidad_sublimacion(cantidad) values
('50'),
('100'),
('150'),
('OTRA');

create table if not exists sublimacion (
    id bigint auto_increment not null primary key,
    cantidad_personalizada varchar(255) null,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 5000,
    precio int null,
    informacion_adicional varchar(255) null,
    id_material_sublimacion bigint not null,
    id_cantidad_sublimacion bigint null,
    constraint fk_material_sublimacion foreign key(id_material_sublimacion) references material_sublimacion(id),
    constraint fk_cantidad_sublimacion foreign key(id_cantidad_sublimacion) references cantidad_sublimacion(id)
);

create table if not exists plantilla_sublimacion (
    id bigint auto_increment not null primary key,
    precio int not null,
    id_material_sublimacion bigint not null,
    id_cantidad_sublimacion bigint null,
    constraint fk_plantilla_material_sublimacion foreign key(id_material_sublimacion) references material_sublimacion(id),
    constraint fk_plantilla_cantidad_sublimacion foreign key(id_cantidad_sublimacion) references cantidad_sublimacion(id)
);

insert into plantilla_sublimacion(id_material_sublimacion, id_cantidad_sublimacion, precio) values
(3, null, 15450),  -- taza de cerámica
(2, null, 11600),  -- taza de polímero
(5, null, 19500),  -- taza mágica
(4, null, 26400),  -- taza con glitter
(8, null, 15450),  -- mate de cerámica
(7, null, 12400),  -- mate de polímero
(9, null, 13900),  -- jarro de café
(10, null, 6750),  -- lapicero de polímero
(14, null, 11050), -- mousepad
(1, 1, 53000),     -- birome, 50 u
(1, 2, 90150),     -- birome, 100 u
(1, 3, 106100);    -- birome, 150 u

-- talonarios (presupuestos, x, recibos)
create table if not exists modo_talonario (
    id bigint auto_increment not null primary key,
    modo varchar(255) not null
);

insert into modo_talonario(modo) values
('ORIGINAL'),
('ORIGINAL + DUPLICADO'),
('N/A');

create table if not exists tipo_talonario (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_talonario(tipo) values
('RECIBO'),
('PRESUPUESTO'),
('X'),
('OTRO');

create table if not exists tipo_papel_talonario (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_papel_talonario(tipo) values
('OBRA 75 GRS'),
('ILUSTRACIÓN 150 GRS');

create table if not exists medida_talonario (
    id bigint auto_increment not null primary key,
    medida varchar(255) not null
);

insert into medida_talonario(medida) values
('1/4 DE A4'),
('A4'),
('A5'),
('1/2 DE A4'),
('13X18 CM'),
('OTRA');

create table if not exists tipo_troquelado_talonario (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_troquelado_talonario(tipo) values
('SIN TROQUELAR'),
('TROQUELADO SIMPLE'),
('TROQUELADO DOBLE');

create table if not exists tipo_color_talonario (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_color_talonario(tipo) values
('BLANCO Y NEGRO'),
('A COLOR');

create table if not exists cantidad_talonario (
    id bigint auto_increment not null primary key,
    cantidad varchar(255) not null
);

insert into cantidad_talonario(cantidad) values
('1'),
('2'),
('4'),
('8'),
('OTRA');

create table if not exists talonario (
    id bigint auto_increment not null primary key,
    con_numerado tinyint(1) not null default 0,
    cantidad_hojas int not null,
    detalle_numerado varchar(255) null,
    es_encolado tinyint(1) not null default 0,
    medida_personalizada varchar(255) null,
    cantidad_personalizada varchar(255) null,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 5000,
    precio int null,
    informacion_adicional varchar(255) null,
    id_tipo_talonario bigint not null,
    id_tipo_troquelado_talonario bigint not null,
    id_modo_talonario bigint not null,
    id_tipo_color_talonario bigint not null,
    id_medida_talonario bigint not null,
    id_tipo_papel_talonario bigint not null,
    id_cantidad_talonario bigint not null,
    constraint fk_tipo_talonario foreign key(id_tipo_talonario) references tipo_talonario(id),
    constraint fk_tipo_troquelado_talonario foreign key(id_tipo_troquelado_talonario) references tipo_troquelado_talonario(id),
    constraint fk_modo_talonario foreign key(id_modo_talonario) references modo_talonario(id),
    constraint fk_tipo_color_talonario foreign key(id_tipo_color_talonario) references tipo_color_talonario(id),
    constraint fk_medida_talonario foreign key(id_medida_talonario) references medida_talonario(id),
    constraint fk_tipo_papel_talonario foreign key(id_tipo_papel_talonario) references tipo_papel_talonario(id),
    constraint fk_cantidad_talonario foreign key(id_cantidad_talonario) references cantidad_talonario(id)
);

create table if not exists plantilla_talonario (
    id bigint auto_increment not null primary key,
    precio int not null,
    cantidad_hojas int not null,
    con_numerado tinyint(1) not null,
    id_tipo_talonario bigint not null,
    id_tipo_troquelado_talonario bigint not null,
    id_modo_talonario bigint not null,
    id_tipo_color_talonario bigint not null,
    id_medida_talonario bigint not null,
    id_tipo_papel_talonario bigint not null,
    id_cantidad_talonario bigint not null,
    constraint fk_plantilla_tipo_talonario foreign key(id_tipo_talonario) references tipo_talonario(id),
    constraint fk_plantilla_tipo_troquelado_talonario foreign key(id_tipo_troquelado_talonario) references tipo_troquelado_talonario(id),
    constraint fk_plantilla_modo_talonario foreign key(id_modo_talonario) references modo_talonario(id),
    constraint fk_plantilla_tipo_color_talonario foreign key(id_tipo_color_talonario) references tipo_color_talonario(id),
    constraint fk_plantilla_medida_talonario foreign key(id_medida_talonario) references medida_talonario(id),
    constraint fk_plantilla_tipo_papel_talonario foreign key(id_tipo_papel_talonario) references tipo_papel_talonario(id),
    constraint fk_plantilla_cantidad_talonario foreign key(id_cantidad_talonario) references cantidad_talonario(id)
);

insert into plantilla_talonario(cantidad_hojas, con_numerado, id_tipo_talonario, id_tipo_troquelado_talonario, id_modo_talonario, id_tipo_color_talonario, id_medida_talonario, id_tipo_papel_talonario, id_cantidad_talonario, precio) values
(50, 1, 2, 2, 2, 1, 3, 1, 1, 13100),  -- 50 hojas, numerado, presupuesto, troquelado, or + 2, byn, a5, obra, 1 u
(50, 1, 2, 2, 2, 1, 3, 1, 2, 23100),  -- 50 hojas, numerado, presupuesto, troquelado, or + 2, byn, a5, obra, 2 u
(50, 1, 2, 2, 2, 1, 3, 1, 3, 41800),  -- 50 hojas, numerado, preuspuesto, troquelado, or + 2, byn, a5, obra, 4 u
(100, 1, 2, 2, 1, 1, 3, 1, 1, 13100), -- 100 hojas, numerado, presupuesto, troquelado, or, byn, a5, obra, 1 u
(100, 1, 2, 2, 1, 1, 3, 1, 2, 23100), -- 100 hojas, numerado, presupuesto, troquelado, or, byn, a5, obra, 2 u
(100, 1, 2, 2, 1, 1, 3, 1, 3, 41800), -- 100 hojas, numerado, presupuesto, troquelado, or, byn, a5, obra, 4 u
(50, 1, 2, 2, 2, 1, 5, 1, 2, 22500),  -- 50 hojas, numerado, presupuesto, troquelado, or + 2, byn, 13x18, obra, 2 u
(50, 1, 2, 2, 2, 1, 5, 1, 3, 38000),  -- 50 hojas, numerado, presupuesto, troquelado, or + 2, byn, 13x18, obra, 4 u
(50, 1, 2, 2, 2, 1, 5, 1, 4, 72500),  -- 50 hojas, numerado, presupuesto, troquelado, or + 2, byn, 13x18, obra, 8 u
(100, 1, 2, 2, 1, 1, 5, 1, 2, 22500), -- 100 hojas, numerado, presupuesto, troquelado, or, byn, 13x18, obra, 2 u
(100, 1, 2, 2, 1, 1, 5, 1, 3, 38000), -- 100 hojas, numerado, presupuesto, troquelado, or, byn, 13x18, obra, 4 u
(100, 1, 2, 2, 1, 1, 5, 1, 4, 72500); -- 100 hojas, numerado, presupuesto, troquelado, or, byn, 13x18, obra, 8 u

-- tarjetas
create table if not exists tipo_color_tarjeta (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_color_tarjeta(tipo) values
('BLANCO Y NEGRO'),
('A COLOR');

create table if not exists tipo_papel_tarjeta (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_papel_tarjeta(tipo) values
('OPALINA 180 GRS'),
('OPALINA 210 GRS'),
('ILUSTRACIÓN 250 GRS'),
('ILUSTRACIÓN 300 GRS');

create table if not exists medida_tarjeta (
    id bigint auto_increment not null primary key,
    medida varchar(255) not null
);

insert into medida_tarjeta(medida) values
('9X5 CM'),
('OTRA');

create table if not exists cantidad_tarjeta (
    id bigint auto_increment not null primary key,
    cantidad varchar(255) not null
);

insert into cantidad_tarjeta(cantidad) values
('50'),
('100'),
('150'),
('200'),
('300'),
('400'),
('500'),
('1000'),
('OTRA');

create table if not exists tipo_laminado_tarjeta (
    id bigint auto_increment not null primary key,
    laminado varchar(255) not null
);

insert into tipo_laminado_tarjeta(laminado) values
('BRILLANTE'),
('MATE'),
('NINGUNO');

create table if not exists tipo_faz_tarjeta (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_faz_tarjeta(tipo) values
('SIMPLE FAZ'),
('DOBLE FAZ');

create table if not exists tarjeta (
    id bigint auto_increment not null primary key,
    medida_personalizada varchar(255) null,
    cantidad_personalizada varchar(255) null,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 5000,
    precio int null,
    informacion_adicional varchar(255) null
    id_tipo_papel_tarjeta bigint not null,
    id_tipo_color_tarjeta bigint not null,
    id_tipo_faz_tarjeta bigint not null,
    id_tipo_laminado_tarjeta bigint not null,
    id_medida_tarjeta bigint not null,
    id_cantidad_tarjeta bigint not null,
    constraint fk_tipo_papel_tarjeta foreign key(id_tipo_papel_tarjeta) references tipo_papel_tarjeta(id),
    constraint fk_tipo_color_tarjeta foreign key(id_tipo_color_tarjeta) references tipo_color_tarjeta(id),
    constraint fk_tipo_faz_tarjeta foreign key(id_tipo_faz_tarjeta) references tipo_faz_tarjeta(id),
    constraint fk_tipo_laminado_tarjeta foreign key(id_tipo_laminado_tarjeta) references tipo_laminado_tarjeta(id),
    constraint fk_medida_tarjeta foreign key(id_medida_tarjeta) references medida_tarjeta(id),
    constraint fk_cantidad_tarjeta foreign key(id_cantidad_tarjeta) references cantidad_tarjeta(id)
);

create table if not exists plantilla_tarjeta (
    id bigint auto_increment not null primary key,
    precio int not null,
    id_tipo_papel_tarjeta bigint not null,
    id_tipo_color_tarjeta bigint not null,
    id_tipo_faz_tarjeta bigint not null,
    id_tipo_laminado_tarjeta bigint not null,
    id_medida_tarjeta bigint not null,
    id_cantidad_tarjeta bigint not null,
    constraint fk_plantilla_tipo_papel_tarjeta foreign key(id_tipo_papel_tarjeta) references tipo_papel_tarjeta(id),
    constraint fk_plantilla_tipo_color_tarjeta foreign key(id_tipo_color_tarjeta) references tipo_color_tarjeta(id),
    constraint fk_plantilla_tipo_faz_tarjeta foreign key(id_tipo_faz_tarjeta) references tipo_faz_tarjeta(id),
    constraint fk_plantilla_tipo_laminado_tarjeta foreign key(id_tipo_laminado_tarjeta) references tipo_laminado_tarjeta(id),
    constraint fk_plantilla_medida_tarjeta foreign key(id_medida_tarjeta) references medida_tarjeta(id),
    constraint fk_plantilla_cantidad_tarjeta foreign key(id_cantidad_tarjeta) references cantidad_tarjeta(id)
);

insert into plantilla_tarjeta(id_tipo_papel_tarjeta, id_tipo_color_tarjeta, id_tipo_faz_tarjeta, id_tipo_laminado_tarjeta, id_medida_tarjeta, id_cantidad_tarjeta, precio) values
(3, 2, 1, 3, 1, 1, 10290), -- 250 grs, color, simple, sin laminar, 9x5, 50
(3, 2, 2, 3, 1, 1, 11350), -- 250 grs, color, doble, sin laminar, 9x5, 50
(3, 2, 1, 2, 1, 1, 12820), -- 250 grs, color, simple, mate, 9x5, 50
(3, 2, 2, 2, 1, 1, 13880), -- 250 grs, color, doble, mate, 9x5, 50
(3, 2, 1, 1, 1, 1, 12270), -- 250 grs, color, simple, brillante, 9x5, 50
(3, 2, 2, 1, 1, 1, 13330), -- 250 grs, color, doble, brillante, 9x5, 50
(3, 2, 1, 3, 1, 2, 16500), -- 250 grs, color, simple, sin laminar, 9x5, 100
(3, 2, 2, 3, 1, 2, 19950), -- 250 grs, color, doble, sin laminar, 9x5, 100
(3, 2, 1, 2, 1, 2, 19800), -- 250 grs, color, simple, mate, 9x5, 100
(3, 2, 2, 2, 1, 2, 23250), -- 250 grs, color, doble, mate, 9x5, 100
(3, 2, 1, 1, 1, 2, 19140), -- 250 grs, color, simple, brillante, 9x5, 100
(3, 2, 2, 1, 1, 2, 22590), -- 250 grs, color, doble, brillante, 9x5, 100
(3, 2, 1, 3, 1, 4, 27560), -- 250 grs, color, simple, sin laminar, 9x5, 200
(3, 2, 2, 3, 1, 4, 31850), -- 250 grs, color, doble, sin laminar, 9x5, 200
(3, 2, 1, 2, 1, 4, 34160), -- 250 grs, color, simple, mate, 9x5, 200
(3, 2, 2, 2, 1, 4, 38450), -- 250 grs, color, doble, mate, 9x5, 200
(3, 2, 1, 1, 1, 4, 32840), -- 250 grs, color, simple, brillante, 9x5, 200
(3, 2, 2, 1, 1, 4, 37130), -- 250 grs, color, doble, brillante, 9x5, 200
(3, 2, 1, 3, 1, 7, 59100), -- 250 grs, color, simple, sin laminar, 9x5, 500
(3, 2, 2, 3, 1, 7, 69000), -- 250 grs, color, doble, sin laminar, 9x5, 500
(3, 2, 1, 2, 1, 7, 75600), -- 250 grs, color, simple, mate, 9x5, 500
(3, 2, 2, 2, 1, 7, 85500), -- 250 grs, color, doble, mate, 9x5, 500
(3, 2, 1, 1, 1, 7, 72300), -- 250 grs, color, simple, brillante, 9x5, 500
(3, 2, 2, 1, 1, 7, 82200); -- 250 grs, color, doble, brillante, 9x5, 500

-- turneros / r.p
create table if not exists tipo_color_turnero (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_color_turnero(tipo) values
('BLANCO Y NEGRO'),
('A COLOR');

create table if not exists cantidad_turnero (
    id bigint auto_increment not null primary key,
    cantidad varchar(255) not null
);

insert into cantidad_turnero(cantidad) values
('2'),
('4'),
('8'),
('12'),
('OTRA');

create table if not exists medida_turnero (
    id bigint auto_increment not null primary key,
    medida varchar(255) not null
);

insert into medida_turnero(medida) values
('1/4 DE A4'),
('13X18 CM'),
('7X8,5 CM'),
('OTRA');

create table if not exists turnero (
    id bigint auto_increment not null primary key,
    cantidad_personalizada varchar(255) null,
    medida_personalizada varchar(255) null,
    cantidad_hojas int null,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 5000,
    precio int null,
    informacion_adicional varchar(255) null,
    id_tipo_color_turnero bigint not null,
    id_cantidad_turnero bigint not null,
    id_medida_turnero bigint not null,
    constraint fk_tipo_color_turnero foreign key(id_tipo_color_turnero) references tipo_color_turnero(id),
    constraint fk_cantidad_turnero foreign key(id_cantidad_turnero) references cantidad_turnero(id),
    constraint fk_medida_turnero foreign key(id_medida_turnero) references medida_turnero(id)
);

create table if not exists plantilla_turnero (
    id bigint auto_increment not null primary key,
    precio int not null,
    cantidad_hojas int null,
    id_tipo_color_turnero bigint not null,
    id_cantidad_turnero bigint not null,
    id_medida_turnero bigint not null,
    constraint fk_plantilla_tipo_color_turnero foreign key(id_tipo_color_turnero) references tipo_color_turnero(id),
    constraint fk_plantilla_cantidad_turnero foreign key(id_cantidad_turnero) references cantidad_turnero(id),
    constraint fk_plantilla_medida_turnero foreign key(id_medida_turnero) references medida_turnero(id)
);

insert into plantilla_turnero(cantidad_hojas, id_tipo_color_turnero, id_cantidad_turnero, id_medida_turnero, precio) values
(100, 1, 2, 1, 29300),  -- 100 hojas, byn, 4, 1/4 de a4
(100, 1, 3, 1, 48000),  -- 100 hojas, byn, 8, 1/4 de a4
(100, 1, 4, 1, 63950),  -- 100 hojas, byn, 12, 1/4 de a4
(100, 2, 2, 1, 38600),  -- 100 hojas, color, 4, 1/4 de a4
(100, 2, 3, 1, 69150),  -- 100 hojas, color, 8, 1/4 de a4
(100, 2, 4, 1, 96100),  -- 100 hojas, color, 12, 1/4 de a4
(100, 1, 1, 2, 22150),  -- 100 hojas, byn, 2, 13x18
(100, 1, 2, 2, 42000),  -- 100 hojas, byn, 4, 13x18
(100, 1, 3, 2, 79150),  -- 100 hojas, byn, 8, 13x18
(100, 1, 4, 2, 112150), -- 100 hojas, byn, 12, 13x18
(100, 2, 1, 2, 25400),  -- 100 hojas, color, 2, 13x18
(100, 2, 2, 2, 46700),  -- 100 hojas, color, 4, 13x18
(100, 2, 3, 2, 89300),  -- 100 hojas, color, 8, 13x18
(100, 2, 4, 2, 125600), -- 100 hojas, color, 12, 13x18
(null, 1, 2, 3, 15850), -- null, byn, 4, 7x8,5
(null, 1, 3, 3, 27650), -- null, byn, 8, 7x8,5
(null, 1, 4, 3, 35900), -- null, byn, 12, 7x8,5
(null, 2, 2, 3, 19850), -- null, color, 4, 7x8,5
(null, 2, 3, 3, 33700), -- null, color, 8, 7x8,5
(null, 2, 4, 3, 45700); -- null, color, 12, 7x8,5

-- vinilos
create table if not exists tipo_corte_vinilo (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_corte_vinilo(tipo) values
('MANUAL'),
('TROQUELADO'),
('TROQUELADO CON CORTE INDIVIDUAL'),
('PLANCHA IMPRESA');

create table if not exists tipo_adicional_vinilo (
    id bigint auto_increment not null primary key,
    adicional varchar(255) not null
);

insert into tipo_adicional_vinilo(adicional) values
('LAMINADO'),
('IMÁN'),
('NINGUNO');

create table if not exists tipo_vinilo (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_vinilo(tipo) values
('ARLON BRILLANTE'),
('ARLON MATE'),
('CLEAR'),
('MICROPERFORADO');

create table if not exists medida_vinilo (
    id bigint auto_increment not null primary key,
    medida varchar(255) not null
);

insert into medida_vinilo(medida) values
('5CM'),
('6CM'),
('7CM'),
('OTRA');

create table if not exists cantidad_vinilo (
    id bigint auto_increment not null primary key,
    cantidad varchar(255) not null
);

insert into cantidad_vinilo(cantidad) values
('50'),
('100'),
('OTRA');

create table if not exists vinilo (
    id bigint auto_increment not null primary key,
    medida_personalizada varchar(255) null,
    cantidad_personalizada varchar(255) null,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 5000,
    precio int null,
    informacion_adicional varchar(255) null,
    id_tipo_vinilo bigint null,
    id_tipo_adicional_vinilo bigint not null,
    id_tipo_corte_vinilo bigint not null,
    id_medida_vinilo bigint not null,
    id_cantidad_vinilo bigint not null,
    constraint fk_tipo_vinilo foreign key(id_tipo_vinilo) references tipo_vinilo(id),
    constraint fk_tipo_adicioal_vinilo foreign key(id_tipo_adicional_vinilo) references tipo_adicional_vinilo(id),
    constraint fk_tipo_corte_vinilo foreign key(id_tipo_corte_vinilo) references tipo_corte_vinilo(id),
    constraint fk_medida_vinilo foreign key(id_medida_vinilo) references medida_vinilo(id),
    constraint fk_cantidad_vinilo foreign key(id_cantidad_vinilo) references cantidad_vinilo(id)
);

create table if not exists plantilla_vinilo (
    id bigint auto_increment not null primary key,
    precio int not null,
    id_tipo_vinilo bigint null,
    id_tipo_adicional_vinilo bigint not null,
    id_tipo_corte_vinilo bigint not null,
    id_medida_vinilo bigint not null,
    id_cantidad_vinilo bigint not null,
    constraint fk_plantilla_tipo_vinilo foreign key(id_tipo_vinilo) references tipo_vinilo(id),
    constraint fk_plantilla_tipo_adicioal_vinilo foreign key(id_tipo_adicional_vinilo) references tipo_adicional_vinilo(id),
    constraint fk_plantilla_tipo_corte_vinilo foreign key(id_tipo_corte_vinilo) references tipo_corte_vinilo(id),
    constraint fk_plantilla_medida_vinilo foreign key(id_medida_vinilo) references medida_vinilo(id),
    constraint fk_plantilla_cantidad_vinilo foreign key(id_cantidad_vinilo) references cantidad_vinilo(id)
);

insert into plantilla_vinilo(id_tipo_vinilo, id_tipo_adicional_vinilo, id_tipo_corte_vinilo, id_medida_vinilo, id_cantidad_vinilo, precio) values
(null, 3, 1, 1, 1, 29800), -- null, sin laminar, manual, 5cm, 50 u
(null, 3, 2, 1, 1, 29800), -- null, sin laminar, troquelado, 5cm, 50 u
(null, 3, 3, 1, 1, 29800), -- null, sin laminar, troquelado + corte, 5 cm, 50 u
(null, 3, 4, 1, 1, 29800), -- null, sin laminar, plancha, 5cm, 50 u
(null, 3, 1, 2, 1, 32500), -- null, sin laminar, manual, 6cm, 50 u
(null, 3, 2, 2, 1, 32500), -- null, sin laminar, troquelado, 6cm, 50 u
(null, 3, 3, 2, 1, 32500), -- null, sin laminar, troquelado + corte, 6 cm, 50 u
(null, 3, 4, 2, 1, 32500), -- null, sin laminar, plancha, 6cm, 50 u
(null, 3, 1, 3, 1, 38200), -- null, sin laminar, manual, 7cm, 50 u
(null, 3, 2, 3, 1, 38200), -- null, sin laminar, troquelado, 7cm, 50 u
(null, 3, 3, 3, 1, 38200), -- null, sin laminar, troquelado + corte, 7 cm, 50 u
(null, 3, 4, 3, 1, 38200), -- null, sin laminar, plancha, 7cm, 50 u
(null, 3, 1, 1, 2, 39700), -- null, sin laminar, manual, 5cm, 100 u
(null, 3, 2, 1, 2, 39700), -- null, sin laminar, troquelado, 5cm, 100 u
(null, 3, 3, 1, 2, 39700), -- null, sin laminar, troquelado + corte, 5 cm, 100 u
(null, 3, 4, 1, 2, 39700), -- null, sin laminar, plancha, 5cm, 100 u
(null, 3, 1, 2, 2, 47300), -- null, sin laminar, manual, 6cm, 100 u
(null, 3, 2, 2, 2, 47300), -- null, sin laminar, troquelado, 6cm, 100 u
(null, 3, 3, 2, 2, 47300), -- null, sin laminar, troquelado + corte, 6 cm, 100 u
(null, 3, 4, 2, 2, 47300), -- null, sin laminar, plancha, 6cm, 100 u
(null, 3, 1, 3, 2, 57500), -- null, sin laminar, manual, 7cm, 100 u
(null, 3, 2, 3, 2, 57500), -- null, sin laminar, troquelado, 7cm, 100 u
(null, 3, 3, 3, 2, 57500), -- null, sin laminar, troquelado + corte, 7 cm, 100 u
(null, 3, 4, 3, 2, 57500); -- null, sin laminar, plancha, 7cm, 100 u

-- vinilos + plástico corrugado
create table if not exists medida_vinilo_plastico_corrugado (
    id bigint auto_increment not null primary key,
    medida varchar(255) not null
);

insert into medida_vinilo_plastico_corrugado(medida) values
('120X90 CM'),
('100X70 CM'),
('70X53 CM'),
('60X40 CM'),
('30X40 CM'),
('OTRA');

create table if not exists vinilo_plastico_corrugado (
    id bigint auto_increment not null primary key,
    medida_personalizada varchar(255) null,
    con_ojales tinyint(1) not null default 0,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 0,
    precio int null,
    informacion_adicional varchar(255) null,
    id_medida_vinilo_plastico_corrugado bigint not null,
    constraint fk_medida_vinilo_plastico_corrugado foreign key(id_medida_vinilo_plastico_corrugado) references medida_vinilo_plastico_corrugado(id)
);

create table if not exists plantilla_vinilo_plastico_corrugado (
    id bigint auto_increment not null primary key,
    precio int not null,
    con_ojales tinyint(1) not null,
    id_medida_vinilo_plastico_corrugado bigint not null,
    constraint fk_plantilla_medida_vinilo_plastico_corrugado foreign key(id_medida_vinilo_plastico_corrugado) references medida_vinilo_plastico_corrugado(id)
);

insert into plantilla_vinilo_plastico_corrugado(con_ojales, id_medida_vinilo_plastico_corrugado, precio) values
(1, 1, 32500), -- con ojales, 120x90
(1, 2, 29700), -- con ojales, 100x70
(1, 3, 19500), -- con ojales, 70x53
(1, 4, 17900), -- con ojales, 60x40
(1, 5, 15600); -- con ojales, 30x40

-- vinilos de corte
create table if not exists trae_material_vinilo (
    id bigint auto_increment not null primary key,
    material varchar(255) not null
);

insert into trae_material_vinilo(material) values
('PARA SOLO CORTE'),
('CORTE PELADO TRANSFER'),
('NO APLICA');

create table if not exists vinilo_de_corte (
    id bigint auto_increment not null primary key,
    es_promocional tinyint(1) not null default 0,
    es_oracal tinyint(1) not null default 0,
    codigo_color varchar(255) not null default '-',
    con_colocacion tinyint(1) not null default 0,
    medida varchar(255) not null,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 5000,
    precio int null,
    informacion_adicional varchar(255) null,
    id_trae_material_vinilo bigint not null,
    constraint fk_trae_material_vinilo foreign key(id_trae_material_vinilo) references trae_material_vinilo(id)
);

-- vouchers
create table if not exists medida_voucher (
    id bigint auto_increment not null primary key,
    medida varchar(255) not null
);

insert into medida_voucher(medida) values
('1/4 DE A4'),
('OTRA');

create table if not exists tipo_papel_voucher (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_papel_voucher(tipo) values
('OPALINA 180 GRS'),
('ILUSTRACIÓN 200 GRS'),
('OTRO');

create table if not exists tipo_faz_voucher (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_faz_voucher(tipo) values
('SIMPLE FAZ'),
('DOBLE FAZ');

create table if not exists cantidad_voucher (
    id bigint auto_increment not null primary key,
    cantidad varchar(255) not null
);

insert into cantidad_voucher(cantidad) values
('50'),
('100'),
('150'),
('OTRA');

create table if not exists voucher (
    id bigint auto_increment not null primary key,
    tipo_papel_personalizado varchar(255) null,
    cantidad_personalizada varchar(255) null,
    medida_personalizada varchar(255) null,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 5000,
    precio int null,
    informacion_adicional varchar(255) null,
    id_medida_voucher bigint not null,
    id_tipo_papel_voucher bigint not null,
    id_tipo_faz_voucher bigint not null,
    id_cantidad_voucher bigint not null,
    constraint fk_medida_voucher foreign key(id_medida_voucher) references medida_voucher(id),
    constraint fk_tipo_papel_voucher foreign key(id_tipo_papel_voucher) references tipo_papel_voucher(id),
    constraint fk_tipo_faz_voucher foreign key(id_tipo_faz_voucher) references tipo_faz_voucher(id),
    constraint fk_cantidad_voucher foreign key(id_cantidad_voucher) references cantidad_voucher(id)
);

create table if not exists plantilla_voucher (
    id bigint auto_increment not null primary key,
    precio int not null,
    id_medida_voucher bigint not null,
    id_tipo_papel_voucher bigint not null,
    id_tipo_faz_voucher bigint not null,
    id_cantidad_voucher bigint not null,
    constraint fk_plantilla_medida_voucher foreign key(id_medida_voucher) references medida_voucher(id),
    constraint fk_plantilla_tipo_papel_voucher foreign key(id_tipo_papel_voucher) references tipo_papel_voucher(id),
    constraint fk_plantilla_tipo_faz_voucher foreign key(id_tipo_faz_voucher) references tipo_faz_voucher(id),
    constraint fk_plantilla_cantidad_voucher foreign key(id_cantidad_voucher) references cantidad_voucher(id)
);

insert into plantilla_voucher(id_medida_voucher, id_tipo_papel_voucher, id_tipo_faz_voucher, id_cantidad_voucher, precio) values
(1, 1, 1, 1, 20050), -- 1/4 de a4, opa. 180, simple, 50 u
(1, 1, 1, 2, 33000), -- 1/4 de a4, opa. 180, simple, 100 u
(1, 1, 1, 3, 47800), -- 1/4 de a4, opa. 180, simple, 150 u
(1, 1, 2, 1, 25800), -- 1/4 de a4, opa. 180, doble, 50 u
(1, 1, 2, 2, 39500), -- 1/4 de a4, opa. 180, doble, 100 u
(1, 1, 2, 3, 52200), -- 1/4 de a4, opa. 180, doble, 150 u
(1, 2, 1, 1, 23800), -- 1/4 de a4, ilu. 200, simple, 50 u
(1, 2, 1, 2, 39250), -- 1/4 de a4, ilu. 200, simple, 100 u
(1, 2, 1, 3, 54600), -- 1/4 de a4, ilu. 200, simple, 150 u
(1, 2, 2, 1, 32300), -- 1/4 de a4, ilu. 200, doble, 50 u
(1, 2, 2, 2, 45150), -- 1/4 de a4, ilu. 200, doble, 100 u
(1, 2, 2, 3, 58350); -- 1/4 de a4, ilu. 200, doble, 150 u

-- otros
create table if not exists tipo_color_otro (
    id bigint auto_increment not null primary key,
    tipo varchar(255) not null
);

insert into tipo_color_otro(tipo) values
('BLANCO Y NEGRO'),
('A COLOR');

create table if not exists otro (
    id bigint auto_increment not null primary key,
    medida varchar(255) null,
    enlace_archivo varchar(255) not null default '-',
    con_adicional_disenio tinyint(1) not null default 0,
    precio_adicional_disenio int not null default 5000,
    precio int null,
    informacion_adicional varchar(255) null,
    id_tipo_color_otro bigint not null,
    constraint fk_tipo_color_otro foreign key(id_tipo_color_otro) references tipo_color_otro(id)
);

-- ordenes de trabajo
create table if not exists medio_pago (
    id bigint auto_increment not null primary key,
    medio_de_pago varchar(255) not null
);

insert into medio_pago(medio_de_pago) values
('DÉBITO'),
('CRÉDITO'),
('TRANSFERENCIA'),
('EFECTIVO');

create table if not exists estado_pago (
    id bigint auto_increment not null primary key,
    estado_de_pago varchar(255) not null
);

insert into estado_pago(estado_de_pago) values
('SIN PAGAR'),
('SEÑADO'),
('PAGADO');

create table if not exists estado_orden (
    id bigint auto_increment not null primary key,
    estado_de_orden varchar(255) not null
);

insert into estado_orden(estado_de_orden) values
('TOMADA'),
('EN PROCESO'),
('LISTA PARA RETIRAR'),
('RETIRADA');

create table if not exists orden_trabajo (
    id bigint auto_increment not null primary key,
    nombre_cliente varchar(255) not null,
    telefono_cliente varchar(255) not null,
    es_cuenta_corriente tinyint(1) not null default 0,
    fecha_pedido date not null,
    fecha_muestra date null,
    fecha_entrega date not null,
    hora_entrega varchar(255) null,
    necesita_factura tinyint(1) not null default 0,
    total int not null,
    abonado int not null default 0,
    resta int not null,
    id_medio_pago bigint null,
    id_estado_pago bigint not null default 1,
    id_estado_orden bigint not null default 1,
    id_empleado bigint not null,
    constraint fk_medio_pago_orden foreign key (id_medio_pago) references medio_pago(id),
    constraint fk_estado_pago_orden foreign key (id_estado_pago) references estado_pago(id),
    constraint fk_estado_orden foreign key (id_estado_orden) references estado_orden(id),
    constraint fk_empleado_orden foreign key (id_empleado) references empleado(id)
);

-- tablas puente
create table if not exists orden_agenda (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_agenda bigint not null,
    constraint fk_orden_agenda foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_agenda foreign key(id_agenda) references agenda(id)
);

create table if not exists orden_anotador (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_anotador bigint not null,
    constraint fk_orden_anotador foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_anotador foreign key(id_anotador) references anotador(id)
);

create table if not exists orden_carpeta_solapa (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_carpeta_solapa bigint not null,
    constraint fk_orden_carpeta_solapa foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_carpeta_solapa foreign key(id_carpeta_solapa) references carpeta_solapa(id)
);

create table if not exists orden_catalogo (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_catalogo bigint not null,
    constraint fk_orden_catalogo foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_catalogo foreign key(id_catalogo) references catalogo(id)
);

create table if not exists orden_cierra_bolsas (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_cierra_bolsas bigint not null,
    constraint fk_orden_cierra_bolsas foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_cierra_bolsas foreign key(id_cierra_bolsas) references cierra_bolsas(id)
);

create table if not exists orden_cuaderno_anillado (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_cuaderno_anillado bigint not null,
    constraint fk_orden_cuaderno_anillado foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_cuaderno_anillado foreign key(id_cuaderno_anillado) references cuaderno_anillado(id)
);

create table if not exists orden_combo (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_combo bigint not null,
    constraint fk_orden_combo foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_combo foreign key(id_combo) references combo(id)
);

create table if not exists orden_entrada (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_entrada bigint not null,
    constraint fk_orden_entrada foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_entrada foreign key(id_entrada) references entrada(id)
);

create table if not exists orden_etiqueta (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_etiqueta bigint not null,
    constraint fk_orden_etiqueta foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_etiqueta foreign key(id_etiqueta) references etiqueta(id)
);

create table if not exists orden_flybanner (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_flybanner bigint not null,
    constraint fk_orden_flybanner foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_flyabner foreign key(id_flybanner) references flybanner(id)
);

create table if not exists orden_folleto (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_folleto bigint not null,
    constraint fk_orden_folleto foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_folleto foreign key(id_folleto) references folleto(id)
);

create table if not exists orden_hojas_membreteadas (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_hojas_membreteadas bigint not null,
    constraint fk_orden_hojas_membreteadas foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_hojas_membreteadas foreign key(id_hojas_membreteadas) references hojas_membreteadas(id)
);

create table if not exists orden_impresion (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_impresion bigint not null,
    constraint fk_orden_impresion foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_impresion foreign key(id_impresion) references impresion(id)
);

create table if not exists orden_lona_comun (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_lona_comun bigint not null,
    constraint fk_orden_lona_comun foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_lona_comun foreign key(id_lona_comun) references lona_comun(id)
);

create table if not exists orden_lona_publicitaria(
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_lona_publicitaria bigint not null,
    constraint fk_orden_lona_publicitaria foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_lona_publicitaria foreign key(id_lona_publicitaria) references lona_publicitaria(id)
);

create table if not exists orden_rifas_bonos_contribucion (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_rifas_bonos_contribucion bigint not null,
    constraint fk_orden_rifas_bonos_contribucion foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_rifas_bonos_contribucion foreign key(id_rifas_bonos_contribucion) references rifas_bonos_contribucion(id)
);

create table if not exists orden_rotulacion (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_rotulacion bigint not null,
    constraint fk_orden_rotulacion foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_rotulacion foreign key(id_rotulacion) references rotulacion(id)
);

create table if not exists orden_sello_automatico (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_sello_automatico bigint not null,
    constraint fk_orden_sello_automatico foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_sello_automatico foreign key(id_sello_automatico) references sello_automatico(id)
);

create table if not exists orden_sello_automatico_escolar (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_sello_automatico_escolar bigint not null,
    constraint fk_orden_sello_automatico_escolar foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_sello_automatico_escolar foreign key(id_sello_automatico_escolar) references sello_automatico_escolar(id)
);

create table if not exists orden_sello_madera (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_sello_madera bigint not null,
    constraint fk_orden_sello_madera foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_sello_madera foreign key(id_sello_madera) references sello_madera(id)
);

create table if not exists orden_sobre (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_sobre bigint not null,
    constraint fk_orden_sobre foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_sobre foreign key(id_sobre) references sobre(id)
);

create table if not exists orden_sticker (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_sticker bigint not null,
    constraint fk_orden_sticker foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_sticker foreign key(id_sticker) references sticker(id)
);

create table if not exists orden_sublimacion (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_sublimacion bigint not null,
    constraint fk_orden_sublimacion foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_sublimacion foreign key(id_sublimacion) references sublimacion(id)
);

create table if not exists orden_talonario (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_talonario bigint not null,
    constraint fk_orden_talonario foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_talonario foreign key(id_talonario) references talonario(id)
);

create table if not exists orden_tarjeta (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_tarjeta bigint not null,
    constraint fk_orden_tarjeta foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_tarjeta foreign key(id_tarjeta) references tarjeta(id)
);

create table if not exists orden_turnero (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_turnero bigint not null,
    constraint fk_orden_turnero foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_turnero foreign key(id_turnero) references turnero(id)
);

create table if not exists orden_vinilo (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_vinilo bigint not null,
    constraint fk_orden_vinilo foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_vinilo foreign key(id_vinilo) references vinilo(id)
);

create table if not exists orden_vinilo_plastico_corrugado (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_vinilo_plastico_corrugado bigint not null,
    constraint fk_orden_vinilo_plastico_corrugado foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_vinilo_plastico_corrugado foreign key(id_vinilo_plastico_corrugado) references vinilo_plastico_corrugado(id)
);

create table if not exists orden_vinilo_de_corte (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_vinilo_de_corte bigint not null,
    constraint fk_orden_vinilo_de_corte foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_vinilo_de_corte foreign key(id_vinilo_de_corte) references vinilo_de_corte(id)
);

create table if not exists orden_voucher (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_voucher bigint not null,
    constraint fk_orden_voucher foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_voucher foreign key(id_voucher) references voucher(id)
);

create table if not exists orden_otro (
    id bigint auto_increment not null primary key,
    cantidad int not null default 1,
    id_orden_trabajo bigint not null,
    id_otro bigint not null,
    constraint fk_orden_otro foreign key(id_orden_trabajo) references orden_trabajo(id),
    constraint fk_otro foreign key(id_otro) references otro(id)
);