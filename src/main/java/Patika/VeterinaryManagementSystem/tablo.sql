-- Table: public.animal
-- DROP TABLE IF EXISTS public.animal;

CREATE TABLE IF NOT EXISTS public.animal
(
    date_of_birth date,
    customer_id integer NOT NULL DEFAULT nextval('animal_customer_id_seq'::regclass),
    id bigint NOT NULL DEFAULT nextval('animal_id_seq'::regclass),
    breed character varying(255) COLLATE pg_catalog."default",
    colour character varying(255) COLLATE pg_catalog."default",
    gender character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    species character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT animal_pkey PRIMARY KEY (id),
    CONSTRAINT fk6pvxm5gfjqxclb651be9unswe FOREIGN KEY (customer_id)
        REFERENCES public.customer (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.animal
    OWNER to postgres;

-- Table: public.appointment
-- DROP TABLE IF EXISTS public.appointment;

CREATE TABLE IF NOT EXISTS public.appointment
(
    animal_id integer NOT NULL DEFAULT nextval('appointment_animal_id_seq'::regclass),
    appointment_date timestamp(6) without time zone,
    doctor_id integer NOT NULL DEFAULT nextval('appointment_doctor_id_seq'::regclass),
    id bigint NOT NULL DEFAULT nextval('appointment_id_seq'::regclass),
    CONSTRAINT appointment_pkey PRIMARY KEY (id),
    CONSTRAINT fk2kkeptdxfuextg5ch7xp3ytie FOREIGN KEY (animal_id)
        REFERENCES public.animal (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkoeb98n82eph1dx43v3y2bcmsl FOREIGN KEY (doctor_id)
        REFERENCES public.doctor (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.appointment
    OWNER to postgres;

-- Table: public.available_date
-- DROP TABLE IF EXISTS public.available_date;

CREATE TABLE IF NOT EXISTS public.available_date
(
    available_date date,
    doctor_id integer NOT NULL DEFAULT nextval('available_date_doctor_id_seq'::regclass),
    id bigint NOT NULL DEFAULT nextval('available_date_id_seq'::regclass),
    CONSTRAINT available_date_pkey PRIMARY KEY (id),
    CONSTRAINT fkk0d6pu1wxarsoou0x2e1cc2on FOREIGN KEY (doctor_id)
        REFERENCES public.doctor (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.available_date
    OWNER to postgres;

-- Table: public.customer
-- DROP TABLE IF EXISTS public.customer;

CREATE TABLE IF NOT EXISTS public.customer
(
    id bigint NOT NULL DEFAULT nextval('customer_id_seq'::regclass),
    address character varying(255) COLLATE pg_catalog."default",
    city character varying(255) COLLATE pg_catalog."default",
    mail character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    phone character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT customer_pkey PRIMARY KEY (id)
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.customer
    OWNER to postgres;

-- Table: public.doctor
-- DROP TABLE IF EXISTS public.doctor;

CREATE TABLE IF NOT EXISTS public.doctor
(
    id bigint NOT NULL DEFAULT nextval('doctor_id_seq'::regclass),
    address character varying(255) COLLATE pg_catalog."default",
    city character varying(255) COLLATE pg_catalog."default",
    mail character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    phone character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT doctor_pkey PRIMARY KEY (id)
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.doctor
    OWNER to postgres;

-- Table: public.vaccine
-- DROP TABLE IF EXISTS public.vaccine;

CREATE TABLE IF NOT EXISTS public.vaccine
(
    protection_finish_date date,
    protection_start_date date,
    animal_id integer NOT NULL DEFAULT nextval('vaccine_animal_id_seq'::regclass),
    id bigint NOT NULL DEFAULT nextval('vaccine_id_seq'::regclass),
    code character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT vaccine_pkey PRIMARY KEY (id),
    CONSTRAINT fkne3kmh8y5pcyxwl4u2w9prw6j FOREIGN KEY (animal_id)
        REFERENCES public.animal (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.vaccine
    OWNER to postgres;
