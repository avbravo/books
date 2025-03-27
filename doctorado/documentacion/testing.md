
# Testing


Probar desde Postman odesde el Navegador

http://localhost:9002/cancerdetectorserver/api/analisis/lookup?filter={"user.iduser": {"$eq": 8}}&sort={"iduser": -1}&page=6&size=1&nhc=3.0


Devuelve
```json

[
    {
        "archivo": [
            {
                "active": false,
                "descripcion": "",
                "extension": "",
                "fecha": "2024-10-06T02:39:44.755Z[UTC]",
                "path": ""
            },
            {
                "active": false,
                "descripcion": "",
                "extension": "",
                "fecha": "2024-10-06T02:39:44.755Z[UTC]",
                "path": ""
            },
            {
                "active": false,
                "descripcion": "",
                "extension": "",
                "fecha": "2024-10-06T02:39:44.755Z[UTC]",
                "path": ""
            },
            {
                "active": false,
                "descripcion": "",
                "extension": "",
                "fecha": "2024-10-06T02:39:44.755Z[UTC]",
                "path": ""
            },
            {
                "active": false,
                "descripcion": "",
                "extension": "",
                "fecha": "2024-10-06T02:39:44.755Z[UTC]",
                "path": ""
            },
            {
                "active": false,
                "descripcion": "",
                "extension": "",
                "fecha": "2024-10-06T02:39:44.755Z[UTC]",
                "path": ""
            },
            {
                "active": false,
                "descripcion": "",
                "extension": "",
                "fecha": "2024-10-06T02:39:44.755Z[UTC]",
                "path": ""
            },
            {
                "active": false,
                "descripcion": "",
                "extension": "",
                "fecha": "2024-10-06T02:39:44.755Z[UTC]",
                "path": ""
            },
            {
                "active": false,
                "descripcion": "",
                "extension": "",
                "fecha": "2024-10-06T02:39:44.755Z[UTC]",
                "path": ""
            },
            {
                "active": false,
                "descripcion": "",
                "extension": "",
                "fecha": "2024-10-06T02:39:44.755Z[UTC]",
                "path": ""
            },
            {
                "active": false,
                "descripcion": "",
                "extension": "",
                "fecha": "2024-10-06T02:39:44.755Z[UTC]",
                "path": ""
            }
        ],
        "coordenadas": {},
        "ctpcrpositiva": 0.0,
        "cultivoorina": "Contaminada",
        "diagnostico": {
            "activo": true,
            "diagnostico": "Escasa flora",
            "iddiagnostico": 4
        },
        "edad": 0.0,
        "escalanuggetobservador": 2,
        "etiquetadoimagen": [
            {
                "activo": true,
                "etiquetadoimagen": "Artefacto",
                "idetiquetadoimagen": 1
            }
        ],
        "fecha": "2024-10-06T02:39:00Z[UTC]",
        "id": {
            "date": "2024-10-06T02:40:09Z[UTC]",
            "timestamp": 1728182409
        },
        "imagen11cultivo": false,
        "imagencondiscrepancia": false,
        "motivo": {
            "activo": true,
            "idmotivo": 5,
            "motivo": "Disuaria/Cistitis"
        },
        "nhc": 3.0,
        "numeromuestra": 0,
        "pcrits": [
            {
                "activo": true,
                "idpcrits": 1,
                "pcrits": "No Realizada"
            }
        ],
        "presenciaEpitales": {
            "activo": false,
            "fivetwentynine": false,
            "greaterthan30": false,
            "one": false,
            "twofour": false,
            "valor": 0.0,
            "zero": true
        },
        "presenciaLeucocitos": {
            "activo": false,
            "fivetwentynine": false,
            "greaterthan30": false,
            "one": false,
            "twofour": false,
            "valor": 0.0,
            "zero": true
        },
        "presenciaLevaduras": {
            "activo": false,
            "fivetwentynine": false,
            "greaterthan30": false,
            "one": false,
            "twofour": false,
            "valor": 0.0,
            "zero": true
        },
        "resultadocultivo": [
            {
                "activo": true,
                "idresultadocultivo": 1,
                "resultadocultivo": "Flora habitual"
            },
            {
                "activo": true,
                "idresultadocultivo": 5,
                "resultadocultivo": "Gardnerella"
            }
        ],
        "user": {
            "actionHistory": [
                {
                    "clase": "PerfilFaces",
                    "evento": "editar",
                    "fecha": "2024-03-22T13:20:43.91Z[UTC]",
                    "iduser": 8,
                    "metodo": "editUser"
                }
            ],
            "active": true,
            "cellphone": "65277389",
            "centralView": {
                "active": true,
                "central": "Centro Regional de Azuero",
                "idcentral": 1
            },
            "dateofbirth": "1973-09-16T05:00:00Z[UTC]",
            "email": "avbravo@gmail.com",
            "identificationcard": "7-117-816",
            "iduser": 8,
            "name": "ARISTIDES VILLARREAL BRAVO",
            "password": "XXdCn5xsqdWve/34pCP1lw==",
            "photo": "/sft/file/repository/16933212306876jm93ek.jpg",
            "profile": [
                {
                    "active": true,
                    "applicativeView": {
                        "active": true,
                        "applicative": "Detección de Cancer",
                        "description": "Deteccion de Cancer",
                        "idapplicative": 1,
                        "image": "",
                        "path": "/cancerdetector",
                        "shortname": "CANCER"
                    },
                    "departamentView": {
                        "active": true,
                        "departament": "UTICA",
                        "iddepartament": 1,
                        "shortname": "UTICA"
                    },
                    "role": {
                        "actionHistory": [
                            {
                                "clase": "ProyectoFaces",
                                "evento": "crear",
                                "fecha": "2023-03-13T02:15:44.973Z[UTC]",
                                "iduser": 1,
                                "metodo": "save"
                            }
                        ],
                        "active": true,
                        "idrole": 7,
                        "role": "COLABORADOR"
                    }
                },
                {
                    "active": false,
                    "applicativeView": {
                        "active": true,
                        "applicative": "Detección de Cancer",
                        "description": "Deteccion de Cancer",
                        "idapplicative": 1,
                        "image": "",
                        "path": "/cancerdetector",
                        "shortname": "CANCER"
                    },
                    "departamentView": {
                        "active": true,
                        "departament": "UTICA",
                        "iddepartament": 1,
                        "shortname": "UTICA"
                    },
                    "role": {
                        "actionHistory": [
                            {
                                "clase": "ProyectoFaces",
                                "evento": "crear",
                                "fecha": "2023-03-13T02:15:44.973Z[UTC]",
                                "iduser": 1,
                                "metodo": "save"
                            }
                        ],
                        "active": true,
                        "idrole": 8,
                        "role": "JEFE-UNIDAD"
                    }
                },
                {
                    "active": false,
                    "applicativeView": {
                        "active": true,
                        "applicative": "Detección de Cancer",
                        "description": "Deteccion de Cancer",
                        "idapplicative": 1,
                        "image": "",
                        "path": "/cancerdetector",
                        "shortname": "CANCER"
                    },
                    "departamentView": {
                        "active": true,
                        "departament": "SUBDIRECCION ADMINISTRATIVA",
                        "iddepartament": 3,
                        "shortname": "SUB. ADMINISTRATIVA"
                    },
                    "role": {
                        "actionHistory": [
                            {
                                "clase": "ProyectoFaces",
                                "evento": "crear",
                                "fecha": "2023-03-13T02:15:44.973Z[UTC]",
                                "iduser": 1,
                                "metodo": "save"
                            }
                        ],
                        "active": true,
                        "idrole": 9,
                        "role": "SUBDIRECTOR-ADMINISTRATIVO"
                    }
                },
                {
                    "active": false,
                    "applicativeView": {
                        "active": true,
                        "applicative": "Detección de Cancer",
                        "description": "Deteccion de Cancer",
                        "idapplicative": 1,
                        "image": "",
                        "path": "/cancerdetector",
                        "shortname": "CANCER"
                    },
                    "departamentView": {
                        "active": true,
                        "departament": "SUBDIRECCION ADMINISTRATIVA",
                        "iddepartament": 3,
                        "shortname": "SUB. ADMINISTRATIVA"
                    },
                    "role": {
                        "actionHistory": [
                            {
                                "clase": "RoleFaces",
                                "evento": "crear",
                                "fecha": "2023-05-19T20:46:01.168Z[UTC]",
                                "iduser": 8,
                                "metodo": "save"
                            }
                        ],
                        "active": true,
                        "idrole": 19,
                        "role": "SUPER-USER"
                    }
                }
            ],
            "recibirNotificacion": true,
            "sex": "Masculino",
            "socialsecuritynumber": "7-117-816",
            "theme": "arya",
            "username": "aristides.villarreal"
        }
    }
]

```
