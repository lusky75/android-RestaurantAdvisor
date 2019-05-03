--
-- PostgreSQL database dump
--

-- Dumped from database version 11.2
-- Dumped by pg_dump version 11.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: Menu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Menu" (
    id bigint NOT NULL,
    name text NOT NULL,
    description text,
    price real NOT NULL,
    restaurant_id bigint
);


ALTER TABLE public."Menu" OWNER TO postgres;

--
-- Name: Menu_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Menu_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."Menu_id_seq" OWNER TO postgres;

--
-- Name: Menu_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Menu_id_seq" OWNED BY public."Menu".id;


--
-- Name: Restaurant; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Restaurant" (
    id bigint NOT NULL,
    name text NOT NULL,
    description text,
    location text,
    phone text,
    website text,
    weekhour text,
    weekendhour text,
    rating integer
);


ALTER TABLE public."Restaurant" OWNER TO postgres;

--
-- Name: Restaurant_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Restaurant_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."Restaurant_id_seq" OWNER TO postgres;

--
-- Name: Restaurant_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Restaurant_id_seq" OWNED BY public."Restaurant".id;


--
-- Name: Review; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Review" (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    restaurant_id bigint NOT NULL,
    message text,
    rating integer NOT NULL
);


ALTER TABLE public."Review" OWNER TO postgres;

--
-- Name: Review_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Review_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."Review_id_seq" OWNER TO postgres;

--
-- Name: Review_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Review_id_seq" OWNED BY public."Review".id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    username text NOT NULL,
    password text NOT NULL,
    admin boolean DEFAULT false,
    firstname text,
    lastname text,
    email text,
    city text,
    country text,
    birthday date
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: Menu id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Menu" ALTER COLUMN id SET DEFAULT nextval('public."Menu_id_seq"'::regclass);


--
-- Name: Restaurant id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Restaurant" ALTER COLUMN id SET DEFAULT nextval('public."Restaurant_id_seq"'::regclass);


--
-- Name: Review id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Review" ALTER COLUMN id SET DEFAULT nextval('public."Review_id_seq"'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Data for Name: Menu; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Menu" (id, name, description, price, restaurant_id) FROM stdin;
3	Côte de boeuf	fumée	10	2
5	Canard	\N	10	2
6	Chipolata	\N	0	1
7	Fricassé	\N	15.5	3
\.


--
-- Data for Name: Restaurant; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Restaurant" (id, name, description, location, phone, website, weekhour, weekendhour, rating) FROM stdin;
3	Hogwarts School of Witchcraft and Wizardry	\N	Somewhere	\N	\N	\N	\N	\N
1	La Goulette	\N	Ivry	\N	\N	\N	\N	0
2	Au Chen Pointu	\N	Shenzhen	\N	\N	\N	\N	1
\.


--
-- Data for Name: Review; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Review" (id, user_id, restaurant_id, message, rating) FROM stdin;
1	2	2	Not bad	2
2	1	2	okjbbj	0
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, username, password, admin, firstname, lastname, email, city, country, birthday) FROM stdin;
1	admin	admin	t	ROOT	\N	admin@mail.com	\N	\N	1100-10-10
2	andy	andy	f	andy	khoury	andy@email.com	\N	\N	\N
\.


--
-- Name: Menu_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Menu_id_seq"', 7, true);


--
-- Name: Restaurant_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Restaurant_id_seq"', 3, true);


--
-- Name: Review_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Review_id_seq"', 2, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 2, true);


--
-- Name: Menu Menu_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Menu"
    ADD CONSTRAINT "Menu_pkey" PRIMARY KEY (id);


--
-- Name: Restaurant Restaurant_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Restaurant"
    ADD CONSTRAINT "Restaurant_pkey" PRIMARY KEY (id);


--
-- Name: Review Review_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Review"
    ADD CONSTRAINT "Review_pkey" PRIMARY KEY (id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- PostgreSQL database dump complete
--

