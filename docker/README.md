# Docker environment for the Phoenix CTMS

This environment is not intended for production use, but rather for system testing and evaluation.
It is based on `install.sh` from the [phoenixctms/ctsms](https://github.com/phoenixctms/ctsms) repository.

The Perl environment for `bulk-processor` is incomplete and not functional at this time.

## Building the environment

The Phoenix container is built in two stages. The first stage downloads and compiles the system. The second stage copies the required build artifacts into a `tomcat:8` container, and conifgures that container to run Phoenix.

```bash
docker-compose build
```

## Starting up

See `docker-compose.yml` for the components of the system. Note that the PostgreSQL database state is stored in a named volume.

Start up the environment:

```bash
docker-compose up
```

The first time you bring up the system, you'll need to run the initial database setup.

```
$ docker-compose exec phoenix bash
bash /dbsetup.sh
```

This should run through cleanly, and at the end it will output randomly generated passwords for the user, department and cron (not yet set up in this environment.)

## Logging in

Once the environment is running and initial setup is complete, you can log in.

Go to [http://localhost:8888](http://localhost:8888)

Log in as `phoenix` with the password provided when you ran `dbsetup.sh`.