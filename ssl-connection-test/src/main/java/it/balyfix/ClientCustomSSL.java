package it.balyfix;

import javax.net.ssl.SSLContext;

public class ClientCustomSSL {

//    public final static void main(String[] args) throws Exception {
//        // Trust own CA and all self-signed certs
//        SSLContext sslcontext = SSLContexts.custom()
//                .loadTrustMaterial(new File("my.keystore"), "nopassword".toCharArray(),
//                        new TrustSelfSignedStrategy())
//                .build();
//        // Allow TLSv1 protocol only
//        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
//                sslcontext,
//                new String[] { "TLSv1" },
//                null,
//                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
//        CloseableHttpClient httpclient = HttpClients.custom()
//                .setSSLSocketFactory(sslsf)
//                .build();
//        try {
//
//            HttpGet httpget = new HttpGet("https://httpbin.org/");
//
//            System.out.println("Executing request " + httpget.getRequestLine());
//
//            CloseableHttpResponse response = httpclient.execute(httpget);
//            try {
//                HttpEntity entity = response.getEntity();
//
//                System.out.println("----------------------------------------");
//                System.out.println(response.getStatusLine());
//                EntityUtils.consume(entity);
//            } finally {
//                response.close();
//            }
//        } finally {
//            httpclient.close();
//        }
//    }

}