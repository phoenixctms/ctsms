CREATE OR REPLACE LANGUAGE plperlu;

CREATE OR REPLACE FUNCTION dbtool (VARIADIC CHARACTER VARYING(1024)[]) RETURNS CHARACTER VARYING(1024)
AS $$
    my ($args) = @_;
    my $cmd = "/ctsms/build/ctsms/core/db/dbtool.sh" . join(" ", @$args);
    return qx $cmd;
$$ LANGUAGE plperlu;