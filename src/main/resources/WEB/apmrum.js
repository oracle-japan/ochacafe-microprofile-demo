window.apmrum = (window.apmrum || {});
window.apmrum.serviceName='helidon demo browser';
window.apmrum.webApplication='HelidonWebapp';
window.apmrum.ociDataUploadEndpoint='__DATA_UPLOAD_POINT__';
window.apmrum.OracleAPMPublicDataKey='__PUBLIC_DATA_KEY__';
window.apmrum.traceSupportingEndpoints =  [
    { headers: [ 'APM' ], hostPattern: '.*' },
    { headers: [ ], hostPattern: '.*' } 
  ];
