#!/bin/bash

echo "Testing DNS propagation and domain setup..."
echo ""

echo "1. Checking DNS for root domain..."
nslookup drivertheoryuae.com
echo ""

echo "2. Checking DNS for www subdomain..."
nslookup www.drivertheoryuae.com
echo ""

echo "3. Testing HTTP endpoints (may fail until DNS propagates)..."
echo ""

echo "Testing: https://drivertheoryuae.com/api/health"
curl -s https://drivertheoryuae.com/api/health || echo "Not yet available"
echo ""

echo "Testing: https://www.drivertheoryuae.com/api/health"
curl -s https://www.drivertheoryuae.com/api/health || echo "Not yet available"
echo ""

echo "Testing: https://zwcrxni9.up.railway.app/api/health"
curl -s https://zwcrxni9.up.railway.app/api/health
echo ""

echo "Done! If DNS hasn't propagated yet, wait 15-30 minutes and run this script again."
