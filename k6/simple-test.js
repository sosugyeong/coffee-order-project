import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 50, 
  duration: '20s',
};

export default function () {
  const payload = JSON.stringify({
    userIdentifier: "user1", // 실제 DB에 존재하는 유저
    point: 100
  });

  const params = {
    headers: { 
      'Content-Type': 'application/json',
      'Idempotency-Key': `k6-test-${Math.floor(Math.random() * 10000000)}` 
    }
  };

  const res = http.post(
      "http://host.docker.internal:8080/cafe/point/charge",
      payload,
      params
  );

  check(res, {
    'is status 201 or 200': (r) => r.status === 201 || r.status === 200,
    'is not 500': (r) => r.status !== 500,
  });

  sleep(0.1); 
}