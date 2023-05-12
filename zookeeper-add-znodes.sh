#! /bin/sh

echo "# Add Zookeeper's ZNodes"
docker-compose exec -t zookeeper bash <<EOF
  sleep 0.3s
  ./bin/zkCli.sh
    create /kafka-manager/mutex ""
    create /kafka-manager/mutex/locks ""
    create /kafka-manager/mutex/leases ""
  quit
  sleep 0.3s
exit
EOF