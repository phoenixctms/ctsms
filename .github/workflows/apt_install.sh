#!/bin/bash
# apt-get install with retries (run as root): the runner's Ubuntu mirror sometimes drops out for
# minutes, longer than apt's own Acquire::Retries cover. Packages already downloaded stay in the
# archives dir, so a retry only fetches what is still missing.
for attempt in 1 2 3; do
  if apt-get -q -y -o=Dpkg::Use-Pty=0 install "$@"; then
    exit 0
  fi
  if [ "$attempt" -lt 3 ]; then
    echo "apt-get install failed (attempt $attempt of 3), retrying in 60 s" >&2
    sleep 60
  fi
done
echo "apt-get install failed after 3 attempts" >&2
exit 1
