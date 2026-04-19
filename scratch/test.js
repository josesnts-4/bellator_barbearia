const http = require('http');

const data = JSON.stringify({
  email: 'jose@bellator.com',
  senha: 'segura6022'
});

const options = {
  hostname: 'localhost',
  port: 8080,
  path: '/auth/login',
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Content-Length': data.length
  }
};

const req = http.request(options, res => {
  let body = '';
  res.on('data', d => body += d);
  res.on('end', () => {
    const json = JSON.parse(body);
    const token = json.token;
    
    if (!token) {
        console.log("No token received", json);
        return;
    }

    http.get('http://localhost:8080/admin/agendamentos', {
      headers: { 'Authorization': 'Bearer ' + token }
    }, res2 => {
      let body2 = '';
      res2.on('data', d => body2 += d);
      res2.on('end', () => {
        console.log("ALL APPOINTMENTS:", body2);
      });
    });
  });
});

req.write(data);
req.end();
