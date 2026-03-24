$(document).ready(function(){ 
 var data = [[Passed,0],[Failed,0],[Skipped,1]];
 jQuery.jqplot('chart', [data], {
 seriesColors: ['#7BB661','#E03C31','#21ABCD'],
 seriesDefaults: {
   renderer: jQuery.jqplot.PieRenderer,
   rendererOptions: {
     padding: 15,
     sliceMargin: 1,
     showDataLabels: true
   }
 },
 grid: { borderColor: '#cccccc', background: '#ffffff', shadow: false },
 legend: { show: true, location: 'e' }
 });
});
