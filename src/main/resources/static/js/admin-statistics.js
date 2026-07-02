const statisticsCharts = {};

document.addEventListener('DOMContentLoaded', () => {
  if (typeof Chart === 'undefined') {
    document.querySelectorAll('.chart-card').forEach((card) => {
      card.classList.add('chart-unavailable');
    });
    return;
  }

  Chart.register(centerTextPlugin);
  initializeCharts();
  bindStatisticsControls();
});

const centerTextPlugin = {
  id: 'centerText',
  afterDraw(chart) {
    const value = chart.canvas.dataset.centerValue;
    if (!value) return;

    const { ctx, chartArea } = chart;
    const x = (chartArea.left + chartArea.right) / 2;
    const y = (chartArea.top + chartArea.bottom) / 2;

    ctx.save();
    ctx.textAlign = 'center';
    ctx.fillStyle = '#8b8b8b';
    ctx.font = '700 12px sans-serif';
    ctx.fillText('총 매출', x, y - 8);
    ctx.fillStyle = '#111111';
    ctx.font = '800 15px sans-serif';
    ctx.fillText(value, x, y + 14);
    ctx.restore();
  },
};

function initializeCharts() {
  statisticsCharts.ageSalesChart = renderBarChart('ageSalesChart');
  statisticsCharts.categorySalesChart = renderDoughnutChart('categorySalesChart');
  statisticsCharts.genderSalesChart = renderDoughnutChart('genderSalesChart');
  statisticsCharts.priceSalesChart = renderBarChart('priceSalesChart');
}

function bindStatisticsControls() {
  const startDateInput = document.getElementById('statisticsStartDate');
  const endDateInput = document.getElementById('statisticsEndDate');
  const searchButton = document.getElementById('statisticsSearchButton');
  const periodSelect = document.getElementById('statisticsPeriodSelect');

  syncDateInputLimits(startDateInput, endDateInput);

  if (searchButton) {
    searchButton.addEventListener('click', () => {
      if (!isDateRangeValid(startDateInput, endDateInput)) return;

      loadStatisticsCharts(startDateInput.value, endDateInput.value);
    });
  }

  [startDateInput, endDateInput].forEach((dateInput) => {
    if (dateInput && periodSelect) {
      dateInput.addEventListener('change', () => {
        syncDateInputLimits(startDateInput, endDateInput);
        periodSelect.value = 'custom';
      });
    }
  });

  if (periodSelect) {
    periodSelect.addEventListener('change', () => {
      const range = selectedDateRange(periodSelect.value);
      if (!range) return;

      startDateInput.value = range.startDate;
      endDateInput.value = range.endDate;
      syncDateInputLimits(startDateInput, endDateInput);
      loadStatisticsCharts(range.startDate, range.endDate);
    });
  }
}

function selectedDateRange(value) {
  const today = startOfToday();
  if (value === 'today') {
    return { startDate: toDateInputValue(today), endDate: toDateInputValue(today) };
  }
  if (value === 'recent-7-days') {
    return { startDate: toDateInputValue(addDays(today, -6)), endDate: toDateInputValue(today) };
  }
  if (value === 'this-month') {
    return {
      startDate: toDateInputValue(new Date(today.getFullYear(), today.getMonth(), 1)),
      endDate: toDateInputValue(today),
    };
  }
  if (value === 'recent-3-months') {
    return { startDate: toDateInputValue(addMonths(today, -3)), endDate: toDateInputValue(today) };
  }
  return null;
}

function loadStatisticsCharts(startDate, endDate) {
  if (!startDate || !endDate) return;

  fetch(
    `/admin/api/statistics?startDate=${encodeURIComponent(startDate)}&endDate=${encodeURIComponent(endDate)}`,
  )
    .then((response) => response.json())
    .then((response) => {
      if (!response.isSuccess) {
        throw new Error(response.message || '통계 데이터를 불러오지 못했습니다.');
      }
      updateStatisticsCharts(response.result);
    })
    .catch((error) => {
      console.error(error);
    });
}

function updateStatisticsCharts(statistics) {
  const startDateInput = document.getElementById('statisticsStartDate');
  const endDateInput = document.getElementById('statisticsEndDate');

  startDateInput.value = statistics.periodStart;
  endDateInput.value = statistics.periodEnd;
  syncDateInputLimits(startDateInput, endDateInput);

  updateBarChart('ageSalesChart', 'ageSalesChartTitle', statistics.ageSalesChart);
  updateDoughnutChart(
    'categorySalesChart',
    'categorySalesChartTitle',
    'categorySalesLegend',
    statistics.categorySalesChart,
  );
  updateDoughnutChart(
    'genderSalesChart',
    'genderSalesChartTitle',
    'genderSalesLegend',
    statistics.genderSalesChart,
  );
  updateBarChart('priceSalesChart', 'priceSalesChartTitle', statistics.priceSalesChart);
}

function renderBarChart(canvasId) {
  const canvas = document.getElementById(canvasId);
  if (!canvas) return null;

  const chartData = readChartData(canvas);

  return new Chart(canvas, {
    type: 'bar',
    data: {
      labels: chartData.labels,
      datasets: [
        {
          data: chartData.values,
          backgroundColor: chartData.colors,
          borderRadius: 8,
          borderSkipped: false,
          barThickness: 38,
          maxBarThickness: 44,
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { display: false },
        tooltip: {
          displayColors: false,
          callbacks: {
            label(context) {
              return `매출 ${formatCurrency(context.parsed.y)}`;
            },
          },
        },
      },
      scales: {
        x: {
          grid: { display: false },
          ticks: {
            color: '#777777',
            font: { size: 11, weight: '600' },
            maxRotation: 0,
            autoSkip: false,
          },
        },
        y: {
          beginAtZero: true,
          border: { display: false },
          grid: { color: '#eeeeee' },
          ticks: {
            color: '#888888',
            font: { size: 11 },
            callback(value) {
              return Number(value) / 1000;
            },
          },
        },
      },
    },
  });
}

function renderDoughnutChart(canvasId) {
  const canvas = document.getElementById(canvasId);
  if (!canvas) return null;

  const chartData = readChartData(canvas);

  return new Chart(canvas, {
    type: 'doughnut',
    data: {
      labels: chartData.labels,
      datasets: [
        {
          data: chartData.values,
          backgroundColor: chartData.colors,
          borderWidth: 0,
          hoverOffset: 4,
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      cutout: '66%',
      plugins: {
        legend: { display: false },
        tooltip: {
          displayColors: true,
          callbacks: {
            label(context) {
              return `${context.label}: ${formatCurrency(context.parsed)}`;
            },
          },
        },
      },
    },
  });
}

function updateBarChart(canvasId, titleId, chartData) {
  updateChartTitle(titleId, chartData.title);
  setChartData(statisticsCharts[canvasId], chartData);
}

function updateDoughnutChart(canvasId, titleId, legendId, chartData) {
  updateChartTitle(titleId, chartData.title);
  const canvas = document.getElementById(canvasId);
  if (!canvas) return;

  canvas.dataset.centerValue = formatCurrency(totalSalesAmount(chartData.points));
  setChartData(statisticsCharts[canvasId], chartData);
  renderLegend(legendId, chartData.points);
}

function updateChartTitle(titleId, title) {
  const titleElement = document.getElementById(titleId);
  if (titleElement) {
    titleElement.textContent = title;
  }
}

function setChartData(chart, chartData) {
  if (!chart) return;

  const points = chartData.points || [];
  chart.data.labels = points.map((point) => point.label);
  chart.data.datasets[0].data = points.map((point) => point.salesAmount);
  chart.data.datasets[0].backgroundColor = points.map((point) => point.color);
  chart.update();
}

function renderLegend(legendId, points) {
  const legend = document.getElementById(legendId);
  if (!legend) return;

  legend.replaceChildren(...(points || []).map((point) => legendItem(point)));
}

function legendItem(point) {
  const item = document.createElement('li');
  const color = document.createElement('span');
  const label = document.createElement('span');
  const amount = document.createElement('strong');
  const rate = document.createElement('em');

  color.className = 'legend-color';
  color.style.backgroundColor = point.color;
  label.className = 'legend-label';
  label.textContent = point.label;
  amount.textContent = formatCurrency(point.salesAmount);
  rate.textContent = `${point.salesRate}%`;

  item.append(color, label, amount, rate);
  return item;
}

function readChartData(canvas) {
  const labels = splitData(canvas.dataset.labels);
  const values = splitData(canvas.dataset.values).map((value) => Number(value));
  const colors = splitData(canvas.dataset.colors);

  return { labels, values, colors };
}

function splitData(value) {
  if (!value) return [];
  const separator = value.includes('|') ? '|' : ',';
  return value.split(separator).map((item) => item.trim());
}

function syncDateInputLimits(startDateInput, endDateInput) {
  if (!startDateInput || !endDateInput) return;

  startDateInput.max = endDateInput.value || '';
  endDateInput.min = startDateInput.value || '';
  startDateInput.setCustomValidity('');
  endDateInput.setCustomValidity('');
}

function isDateRangeValid(startDateInput, endDateInput) {
  if (!startDateInput || !endDateInput) return false;

  if (startDateInput.value && endDateInput.value && startDateInput.value > endDateInput.value) {
    startDateInput.setCustomValidity('시작날짜는 끝날짜 이후로 선택할 수 없습니다.');
    endDateInput.setCustomValidity('끝날짜는 시작날짜 이전으로 선택할 수 없습니다.');
    startDateInput.reportValidity();
    return false;
  }

  syncDateInputLimits(startDateInput, endDateInput);
  return true;
}

function totalSalesAmount(points) {
  return (points || []).reduce((total, point) => total + Number(point.salesAmount || 0), 0);
}

function formatCurrency(value) {
  return `${Number(value).toLocaleString('ko-KR')}원`;
}

function startOfToday() {
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  return today;
}

function addDays(date, days) {
  const nextDate = new Date(date);
  nextDate.setDate(nextDate.getDate() + days);
  return nextDate;
}

function addMonths(date, months) {
  const targetMonthIndex = date.getMonth() + months;
  const targetYear = date.getFullYear() + Math.floor(targetMonthIndex / 12);
  const targetMonth = ((targetMonthIndex % 12) + 12) % 12;
  const lastDayOfTargetMonth = new Date(targetYear, targetMonth + 1, 0).getDate();
  const targetDay = Math.min(date.getDate(), lastDayOfTargetMonth);

  return new Date(targetYear, targetMonth, targetDay);
}

function toDateInputValue(date) {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}
