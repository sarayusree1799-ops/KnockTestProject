$(document).ready(function(){
 var s1 = [0];
 var s2 = [0];
 var s3 = [1];

var ticks = [1];$.jqplot('bar', [s1, s2, s3], {
 animate: true,
 axesDefaults: { min: 0, tickInterval: 1 },
 seriesColors: ['#7BB661','#E03C31','#21ABCD'],
 seriesDefaults: {
   renderer: $.jqplot.BarRenderer,
   rendererOptions: { barWidth: 12, barMargin: 25, fillToZero: true }
 },
 grid: { borderColor: '#ffffff', background: '#ffffff', shadow: false },
 legend: { show: true, placement: 'outside', location: 'e' },
 labels: ['Passed','Failed','Skipped'],
 axes: {
   xaxis: { renderer: $.jqplot.CategoryAxisRenderer, ticks: ticks, label: 'Run Number' },
   yaxis: { label: 'TC Number', tickOptions: { formatString: '%d' } }
 }
});
